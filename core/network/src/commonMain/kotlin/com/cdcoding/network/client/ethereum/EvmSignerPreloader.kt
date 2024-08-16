package com.cdcoding.network.client.ethereum

import com.cdcoding.common.utils.type
import com.cdcoding.model.Account
import com.cdcoding.model.AssetId
import com.cdcoding.model.AssetSubtype
import com.cdcoding.model.Chain
import com.cdcoding.model.ConfirmParams
import com.cdcoding.model.EVMChain
import com.cdcoding.model.Fee
import com.cdcoding.model.SignerInputInfo
import com.cdcoding.model.SignerParams
import com.cdcoding.model.TransactionType
import com.cdcoding.network.client.SignerPreload
import com.cdcoding.network.model.JSONRpcRequest
import com.cdcoding.network.util.NetworkError
import com.cdcoding.network.util.Result
import com.cdcoding.network.util.fold
import com.cdcoding.wallet.operator.ChainTypeProxy
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.integer.BigInteger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext


class EvmSignerPreloader(
    private val chain: Chain,
    private val apiClient: EvmApiClient,
) : SignerPreload {
    @OptIn(ExperimentalStdlibApi::class)
    override suspend fun invoke(owner: Account, params: ConfirmParams): Result<SignerParams, NetworkError> = withContext(
        Dispatchers.IO) {
        val assetId = if (params.getTxType() == TransactionType.Swap) {
            AssetId(params.assetId.chain)
        } else {
            params.assetId
        }
        val coinType = ChainTypeProxy().invoke(chain)
        val chainIdJob = async {
            apiClient.getNetVersion(JSONRpcRequest.create(EvmMethod.GetNetVersion, emptyList()))
                .fold({ it.result?.value }) { null } ?: BigInteger.parseString(coinType.chainId)
        }
        val nonceJob = async {
            val nonceParams = listOf(owner.address, "latest")
            apiClient.getNonce(JSONRpcRequest.create(EvmMethod.GetNonce, nonceParams))
                .fold({ it.result?.value }) { null } ?: BigInteger.ZERO
        }
        val gasLimitJob = async {
            getGasLimit(
                assetId = assetId,
                apiClient = apiClient,
                from = owner.address,
                recipient = when (params) {
                    is ConfirmParams.SwapParams -> params.to
                    is ConfirmParams.TokenApprovalParams -> params.assetId.tokenId!!
                    is ConfirmParams.TransferParams -> params.to
                    else -> throw IllegalArgumentException()
                },
                outputAmount = when (params) {
                    is ConfirmParams.SwapParams -> BigInteger.parseString(params.value)
                    is ConfirmParams.TokenApprovalParams -> BigInteger.ZERO
                    is ConfirmParams.TransferParams -> params.amount
                    else -> throw IllegalArgumentException()
                },
                payload = when (params) {
                    is ConfirmParams.SwapParams -> params.swapData
                    is ConfirmParams.TokenApprovalParams -> params.approvalData
                    else -> params.memo()
                },
            )
        }
        val chainId = chainIdJob.await()
        val nonce = nonceJob.await()
        val gasLimit = gasLimitJob.await()
        val fee = try {
            EvmFee().invoke(
                apiClient,
                params,
                chainId,
                nonce,
                gasLimit,
                ChainTypeProxy().invoke(chain)
            )
        } catch (err: Throwable) {
            return@withContext Result.Error(NetworkError.UNKNOWN)
        }
        Result.Success(
            SignerParams(
                input = params,
                owner = owner.address,
                info = Info(chainId, nonce, fee),
            )
        )
    }

    override fun maintainChain(): Chain = chain

    @ExperimentalStdlibApi
    private suspend fun getGasLimit(
        assetId: AssetId,
        apiClient: EvmApiClient,
        from: String,
        recipient: String,
        outputAmount: BigInteger,
        payload: String?,
    ): BigInteger {
        val (amount, to, data) = when (assetId.type()) {
            AssetSubtype.NATIVE -> Triple(outputAmount, recipient, payload)
            AssetSubtype.TOKEN -> Triple(
                BigInteger.ZERO,    // Amount
                assetId.tokenId!!.lowercase(), // token
                EVMChain.encodeTransactionData(assetId, payload, outputAmount, recipient)
                    .toHexString()
            )
            else -> throw IllegalArgumentException()
        }
        val transaction = EvmApiClient.Transaction(
            from = from,
            to = to,
            value = "0x${amount.toString(16)}",
            data = if (data.isNullOrEmpty()) "0x" else data,
        )
        val request = JSONRpcRequest.create(EvmMethod.GetGasLimit, listOf(transaction))
        val gasLimitResult = apiClient.getGasLimit(request)
        val gasLimit = gasLimitResult.fold({ it.result?.value ?: BigInteger.ZERO}) {
            BigInteger.ZERO
        }
        return if (gasLimit == BigInteger(21_000L)) {
            gasLimit
        } else {
            gasLimit.add(
                BigDecimal.fromBigInteger(gasLimit).multiply(
                    BigDecimal.fromDouble(0.5)
                ).toBigInteger()
            )
        }
    }

    data class Info(
        val chainId: BigInteger,
        val nonce: BigInteger,
        val fee: Fee,
    ) : SignerInputInfo {
        override fun fee(): Fee = fee
    }
}