package com.cdcoding.network.client.ethereum

import com.cdcoding.common.utils.eip1559Support
import com.cdcoding.common.utils.type
import com.cdcoding.model.AssetId
import com.cdcoding.model.AssetSubtype
import com.cdcoding.model.Chain
import com.cdcoding.model.ConfirmParams
import com.cdcoding.model.EVMChain
import com.cdcoding.model.GasFee
import com.cdcoding.model.SignerParams
import com.cdcoding.network.client.SignClient
import com.cdcoding.wallet.operator.ChainTypeProxy
import com.ionspin.kotlin.bignum.integer.BigInteger
import com.trustwallet.core.AnySigner
import com.trustwallet.core.Blockchain
import com.trustwallet.core.CoinType
import com.trustwallet.core.PrivateKey
import com.trustwallet.core.ethereum.SigningInput
import com.trustwallet.core.ethereum.SigningOutput
import com.trustwallet.core.ethereum.Transaction
import com.trustwallet.core.ethereum.TransactionMode
import com.trustwallet.core.sign
import okio.ByteString.Companion.toByteString


class EvmSignClient(
    private val chain: Chain,
) : SignClient {

    override suspend fun signMessage(input: ByteArray, privateKey: ByteArray): ByteArray {
        val result = PrivateKey(privateKey).sign(input, CoinType.Ethereum.curve) ?: byteArrayOf()
        result[64] = (result[64] + 27).toByte()
        return result
    }

    override suspend fun signTransfer(
        params: SignerParams,
        privateKey: ByteArray,
    ): ByteArray {
        val meta = params.info as EvmSignerPreloader.Info
        val fee = meta.fee as? GasFee ?: throw IllegalArgumentException()
        val coinType = ChainTypeProxy().invoke(chain)
        val input = params.input
        val amount = when (input) {
            is ConfirmParams.SwapParams -> BigInteger.parseString(input.value)
            is ConfirmParams.TokenApprovalParams -> BigInteger.ZERO
            is ConfirmParams.TransferParams -> when (params.input.assetId.type()) {
                AssetSubtype.NATIVE -> params.finalAmount
                AssetSubtype.TOKEN -> BigInteger.ZERO
            }
            else -> throw IllegalArgumentException()
        }
        val signInput = buildSignInput(
            assetId = when (input) {
                is ConfirmParams.SwapParams -> AssetId(input.assetId.chain)
                else -> params.input.assetId
            },
            amount = amount,
            tokenAmount = params.finalAmount,
            fee = fee,
            chainId = meta.chainId,
            nonce = meta.nonce,
            destinationAddress = params.input.destination(),
            memo = when (input) {
                is ConfirmParams.SwapParams -> input.swapData
                is ConfirmParams.TokenApprovalParams -> input.approvalData
                else -> input.memo()
            },
            privateKey = privateKey,
        )
        return AnySigner.sign(signInput, coinType, SigningOutput.ADAPTER)
            .encoded
            .toByteArray()
    }

    internal fun buildSignInput(
        assetId: AssetId,
        amount: BigInteger,
        tokenAmount: BigInteger,
        fee: GasFee,
        chainId: BigInteger,
        nonce: BigInteger,
        destinationAddress: String,
        memo: String?,
        privateKey: ByteArray,
    ): SigningInput {

        return if (chain.eip1559Support()) {
            SigningInput(
                tx_mode = TransactionMode.Enveloped,
                max_fee_per_gas = fee.maxGasPrice.toByteArray().toByteString(),
                max_inclusion_fee_per_gas = fee.minerFee.toByteArray().toByteString(),
                gas_limit = fee.limit.toByteArray().toByteString(),
                chain_id = chainId.toByteArray().toByteString(),
                nonce = nonce.toByteArray().toByteString(),
                to_address = EVMChain.getDestinationAddress(assetId, destinationAddress),
                private_key = privateKey.toByteString(),
                transaction = Transaction(
                    transfer = Transaction.Transfer(
                        amount = amount.toByteArray().toByteString(),
                        data_ = EVMChain.encodeTransactionData(
                            assetId,
                            memo,
                            tokenAmount,
                            destinationAddress
                        ).toByteString()
                    )
                )

            )
        } else {
            SigningInput(
                tx_mode = TransactionMode.Legacy,
                gas_price = fee.maxGasPrice.toByteArray().toByteString(),
                gas_limit = fee.limit.toByteArray().toByteString(),
                chain_id = chainId.toByteArray().toByteString(),
                nonce = nonce.toByteArray().toByteString(),
                to_address = EVMChain.getDestinationAddress(assetId, destinationAddress),
                private_key = privateKey.toByteString(),
                transaction = Transaction(
                    transfer = Transaction.Transfer(
                        amount = amount.toByteArray().toByteString(),
                        data_ = EVMChain.encodeTransactionData(
                            assetId,
                            memo,
                            tokenAmount,
                            destinationAddress
                        ).toByteString()
                    )
                )

            )
        }
    }

    override fun maintainChain(): Chain = chain
}