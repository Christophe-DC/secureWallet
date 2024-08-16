package com.cdcoding.network.client

import com.cdcoding.model.SignerParams


interface SignClient : BlockchainClient {

    suspend fun signMessage(
        input: ByteArray,
        privateKey: ByteArray,
    ): ByteArray = byteArrayOf()

    suspend fun signTransfer(
        params: SignerParams,
        privateKey: ByteArray,
    ): ByteArray
}