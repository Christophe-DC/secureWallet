package com.cdcoding.network.client.ethereum

import com.cdcoding.common.utils.type
import com.cdcoding.model.AssetId
import com.cdcoding.model.AssetSubtype
import com.cdcoding.model.ConfirmParams
import com.cdcoding.model.GasFee
import com.cdcoding.model.TransactionType
import com.cdcoding.network.model.JSONRpcRequest
import com.cdcoding.network.util.getOrNull
import com.ionspin.kotlin.bignum.integer.BigInteger
import com.trustwallet.core.AnySigner
import com.trustwallet.core.CoinType
import com.trustwallet.core.EthereumAbi
import com.trustwallet.core.EthereumAbiFunction
import com.trustwallet.core.PrivateKey
import com.trustwallet.core.ethereum.SigningOutput
import com.trustwallet.core.sign
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext


class OptimismGasOracle {

    suspend operator fun invoke(
        params: ConfirmParams,
        chainId: BigInteger,
        nonce: BigInteger,
        gasLimit: BigInteger,
        coin: CoinType,
        apiClient: EvmApiClient
    ): GasFee = withContext(Dispatchers.IO) {
        val assetId = params.assetId
        val feeAssetId = AssetId(assetId.chain)
        val basePriorityFee = async { EvmFee.getBasePriorityFee(apiClient) }
        val (baseFee, priorityFee) = basePriorityFee.await()
        val gasPrice = baseFee + priorityFee
        val minerFee = when (params.getTxType()) {
            TransactionType.Transfer -> if (assetId.type() == AssetSubtype.NATIVE && params.isMax()) gasPrice else priorityFee
            TransactionType.Swap -> priorityFee
            else -> throw Exception("Operation doesn't available")
        }
        val amount = when (params.getTxType()) {
            TransactionType.Transfer -> if (params.isMax()) params.amount - gasLimit * gasPrice else params.amount
            TransactionType.TokenApproval, TransactionType.Swap -> params.amount
            else -> throw Exception("Operation doesn't available")
        }
        val encoded = encode(
            assetId = assetId,
            coin = coin,
            amount = amount,
            destinationAddress = when (params) {
                is ConfirmParams.SwapParams -> params.to
                is ConfirmParams.TokenApprovalParams -> params.assetId.tokenId!!
                is ConfirmParams.TransferParams -> params.to
                else -> throw IllegalArgumentException()
            },
            meta = when (params) {
                is ConfirmParams.SwapParams -> params.swapData
                is ConfirmParams.TokenApprovalParams -> params.approvalData
                else -> params.memo()
            },
            chainId = chainId,
            nonce = nonce,
            gasFee = GasFee(
                feeAssetId = feeAssetId,
                limit = gasLimit,
                maxGasPrice = gasPrice,
                minerFee = minerFee,
            ),
        )
        val l2Fee = gasPrice * gasLimit
        val l1Fee = getL1Fee(encoded, apiClient)!!
        GasFee(
            feeAssetId = feeAssetId,
            maxGasPrice = gasPrice,
            minerFee = minerFee,
            limit = gasLimit,
            amount = l1Fee + l2Fee,
        )
    }

    class BaseFeeRequest(val to: String, val data: String)

    @OptIn(ExperimentalStdlibApi::class)
    private suspend fun getL1Fee(data: ByteArray, apiClient: EvmApiClient): BigInteger? {
        val abiFn = EthereumAbiFunction("getL1Fee").apply {
            this.addParamBytes(data, false)
        }
        val encodedFn = EthereumAbi.encode(abiFn)
        val request = JSONRpcRequest.create(
            EvmMethod.Call,
            listOf(
                BaseFeeRequest(
                    to = "0x420000000000000000000000000000000000000F", encodedFn.toHexString(),
                ),
                "latest",
            )
        )
        return apiClient.callNumber(request).getOrNull()?.result?.value
    }

    private fun encode(
        assetId: AssetId,
        coin: CoinType,
        destinationAddress: String,
        amount: BigInteger,
        meta: String?,
        chainId: BigInteger,
        nonce: BigInteger,
        gasFee: GasFee,
    ): ByteArray {
        val signInput = EvmSignClient(assetId.chain).buildSignInput(
            assetId = assetId,
            amount = amount,
            tokenAmount = amount,
            fee = gasFee,
            chainId = chainId,
            nonce = nonce,
            destinationAddress = destinationAddress,
            memo = meta,
            privateKey = PrivateKey().data,
        )
        val signer = AnySigner.sign(signInput, coin, SigningOutput.ADAPTER)
        val signatureLenInRlp = 67 // 1 + 32 + 1 + 32 + 1
        val encoded = signer.encoded.toByteArray().toList().dropLast(signatureLenInRlp).toMutableList()
        when (assetId.type()) {
            AssetSubtype.NATIVE -> encoded.removeAt(2)
            else -> {}
        }
        return encoded.toByteArray()
    }
}