package com.cdcoding.network.client.sui

import com.cdcoding.common.utils.decodeHex
import com.cdcoding.model.Chain
import com.cdcoding.model.SignerParams
import com.cdcoding.network.client.SignClient
import com.trustwallet.core.Base64
import com.trustwallet.core.Curve
import com.trustwallet.core.PrivateKey
import io.ktor.utils.io.core.toByteArray


class SuiSignClient(
    private val chain: Chain,
) : SignClient {
    override suspend fun signTransfer(params: SignerParams, privateKey: ByteArray): ByteArray {
        val metadata = params.info as SuiSignerPreloader.Info
        return signTxDataDigest(metadata.messageBytes, privateKey)
    }

    override fun maintainChain(): Chain = chain

    private fun signTxDataDigest(data: String, privateKey: ByteArray): ByteArray {
        val key = PrivateKey(privateKey)
        val pubKey = key.getPublicKeyEd25519()
        val parts = data.split("_")
        val digest = parts[1].decodeHex()
        val signature = key.sign(digest, Curve.ED25519) ?: byteArrayOf(0x0)
        val sig = byteArrayOf(0x0) + signature + pubKey.data
        return "${parts[0]}_${Base64.encode(sig)}".toByteArray()
    }
}