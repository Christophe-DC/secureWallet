package com.cdcoding.network.client

import com.cdcoding.model.Chain
import com.cdcoding.model.SignerParams


interface SignTransfer {
    suspend operator fun invoke(
        input: SignerParams,
        privateKey: ByteArray
    ): Result<ByteArray>

    suspend operator fun invoke(
        chain: Chain,
        input: ByteArray,
        privateKey: ByteArray,
    ): Result<ByteArray>
}