package com.cdcoding.network.client.solana

import com.cdcoding.common.utils.type
import com.cdcoding.model.AssetSubtype
import com.cdcoding.model.Chain
import com.cdcoding.model.ConfirmParams
import com.cdcoding.model.SignerParams
import com.cdcoding.model.TransactionType
import com.cdcoding.network.client.SignClient
import com.trustwallet.core.AnySigner
import com.trustwallet.core.Base58
import com.trustwallet.core.Base64
import com.trustwallet.core.CoinType
import com.trustwallet.core.Curve
import com.trustwallet.core.PrivateKey
import com.trustwallet.core.SolanaAddress
import com.trustwallet.core.sign
import com.trustwallet.core.solana.CreateAndTransferToken
import com.trustwallet.core.solana.SigningInput
import com.trustwallet.core.solana.SigningOutput
import com.trustwallet.core.solana.TokenTransfer
import com.trustwallet.core.solana.Transfer
import io.ktor.utils.io.core.toByteArray
import okio.ByteString.Companion.toByteString


class SolanaSignClient(
) : SignClient {

    override suspend fun signMessage(input: ByteArray, privateKey: ByteArray): ByteArray {
        val str = input.decodeToString()
        val bytes = Base64.decode(str)
        if (bytes == null || bytes[0].toInt() != 1) {
            throw IllegalArgumentException("only support one signature")
        }

        val message = bytes.copyOfRange(65, bytes.size - 1)
        val signature = PrivateKey(privateKey).sign(message, Curve.ED25519)
            ?: throw IllegalArgumentException("signature is null")
        val signed = byteArrayOf(0x1) + signature + message
        return Base64.encode(signed).toByteArray()
    }

    override suspend fun signTransfer(
        params: SignerParams,
        privateKey: ByteArray
    ): ByteArray {
        return when (params.input.getTxType()) {
            TransactionType.Swap -> swap(params, privateKey).toByteArray()
            TransactionType.Transfer -> {
                val signInput = when (params.input.assetId.type()) {
                    AssetSubtype.NATIVE -> signNative(params, privateKey)
                    AssetSubtype.TOKEN -> signToken(params, privateKey)
                }
                sign(message = signInput)
            }

            TransactionType.TokenApproval -> throw IllegalArgumentException()
        }
    }

    private fun signNative(input: SignerParams, privateKey: ByteArray): SigningInput {
        val blockhash = (input.info as SolanaSignerPreloader.Info).blockhash
        return SigningInput(
            private_key = privateKey.toByteString(),
            transfer_transaction = input.input.memo()?.let {
                Transfer(
                    recipient = input.input.destination(),
                    value_ = input.finalAmount.longValue(),
                    memo = it
                )
            } ?: Transfer(
                recipient = input.input.destination(),
                value_ = input.finalAmount.longValue()
            ),
            recent_blockhash = blockhash
        )
    }

    private suspend fun signToken(input: SignerParams, privateKey: ByteArray): SigningInput {
        val decimals = 2// getAsset.getAsset(input.input.assetId)?.decimals ?: throw IllegalArgumentException("Asset not found")
        val tokenId = input.input.assetId.tokenId ?: throw IllegalArgumentException("tokenId is null")
        val amount = input.finalAmount.longValue()
        val recipient = input.input.destination()
        val recipientTokenAddress = SolanaAddress(recipient).defaultTokenAddress(tokenId) ?: throw IllegalArgumentException("recipient Token Address is null")
        val metadata = input.info as SolanaSignerPreloader.Info
        return if(metadata.recipientTokenAddress.isNullOrEmpty()) {
            SigningInput(
                recent_blockhash = metadata.blockhash,
                private_key = privateKey.toByteString(),
                create_and_transfer_token_transaction =
                CreateAndTransferToken(
                    amount = amount,
                    decimals = decimals,
                    recipient_main_address = recipient,
                    token_mint_address = tokenId,
                    sender_token_address = metadata.senderTokenAddress,
                    recipient_token_address = recipientTokenAddress,
                    memo = input.input.memo() ?: ""
                )
            )
        } else {
            SigningInput(
                recent_blockhash = metadata.blockhash,
                private_key = privateKey.toByteString(),
                token_transfer_transaction = TokenTransfer(
                    amount = amount,
                    decimals = decimals,
                    token_mint_address = tokenId,
                    sender_token_address = metadata.senderTokenAddress,
                    recipient_token_address = metadata.recipientTokenAddress,
                    memo = input.input.memo() ?: ""
                )
            )
        }
    }

    private fun swap(input: SignerParams, privateKey: ByteArray): String {
        val swapParams = input.input as ConfirmParams.SwapParams
        val bytes = Base64.decode(swapParams.swapData) ?: throw IllegalArgumentException("swap data is null")
        if (bytes[0] != 1.toByte()) {
            throw IllegalArgumentException("only support one signature")
        }
        val message = bytes.copyOfRange(65, bytes.size)
        val signature = PrivateKey(privateKey).sign(message, Curve.Curve25519) ?: throw IllegalArgumentException("signature is null")
        val signed = byteArrayOf(0x1)
        return Base64.encode(signed + signature + message)
    }

    private fun sign(message: SigningInput): ByteArray {
        val output = AnySigner.sign(message, CoinType.Solana, SigningOutput.ADAPTER)
        val data = Base58.decodeNoCheck(output.encoded)
            ?: throw IllegalStateException("string is not Base58 encoding!")
        val base64 = Base64.encode(data)
        val offset = base64.length % 4
        return (if (offset == 0) base64 else base64.padStart(base64.length + 4 - offset, '='))
            .toByteArray()
    }

    override fun maintainChain(): Chain = Chain.Solana
}
