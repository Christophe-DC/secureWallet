package com.cdcoding.network.client.near

import com.cdcoding.model.Chain
import com.cdcoding.model.SignerParams
import com.cdcoding.network.client.SignClient
import com.cdcoding.wallet.operator.ChainTypeProxy
import com.ionspin.kotlin.bignum.integer.BigInteger
import com.trustwallet.core.AnySigner
import com.trustwallet.core.Base58
import com.trustwallet.core.Base64
import com.trustwallet.core.near.Action
import com.trustwallet.core.near.SigningInput
import com.trustwallet.core.near.SigningOutput
import com.trustwallet.core.near.Transfer
import com.trustwallet.core.sign
import io.ktor.utils.io.core.toByteArray
import okio.ByteString
import okio.ByteString.Companion.toByteString

class NearSignClient(
    private val chain: Chain,
) : SignClient {
    override suspend fun signTransfer(params: SignerParams, privateKey: ByteArray): ByteArray {
        val metadata = params.info as NearSignerPreloader.Info

        val input = SigningInput(
            signer_id =  params.owner,
            nonce = metadata.sequence,
            receiver_id = params.input.destination(),
            actions = listOf(
                Action(
                    transfer = Transfer(
                        deposit = params.finalAmount.littleIndian()?.reversedArray()?.toByteString() ?: ByteString.EMPTY
                    )
                )

            ),
            block_hash = Base58.decodeNoCheck(metadata.block)?.toByteString() ?: ByteString.EMPTY,
            private_key = privateKey.toByteString()
        )
        val output = AnySigner.sign(input, ChainTypeProxy().invoke(chain), SigningOutput.ADAPTER)
        return Base64.encode(output.signed_transaction.toByteArray()).toByteArray()
    }

    override fun maintainChain(): Chain = chain
}



private fun BigInteger.littleIndian(): ByteArray? {
    val isPositive = this >= BigInteger.ZERO
    val byteCount = 128 / 8
    val valueData = this.twos()

    if (valueData.size > byteCount) {
        return null
    }

    val padding = if (isPositive) {
        ByteArray(byteCount - valueData.size) { 0 }
    } else {
        ByteArray(byteCount - valueData.size) { 0xff.toByte() }
    }

    return padding + valueData.reversedArray()
}

fun BigInteger.twos(): ByteArray {
    val contents = this.toByteArray()

    if (this >= BigInteger.ZERO) {
        return contents
    }

    val padding = 0xff.toByte()
    val padded = ByteArray(32) { padding }

    val start = 32 - contents.size
    for (i in contents.indices) {
        padded[start + i] = contents[i]
    }

    return padded
}