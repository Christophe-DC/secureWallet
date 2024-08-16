package com.cdcoding.network.client

import com.cdcoding.model.Chain
import com.cdcoding.model.SignerParams


class SignTransferProxy(
    private val clients: List<SignClient>,
) : SignTransfer {

    override suspend fun invoke(
        input: SignerParams,
        privateKey: ByteArray,
    ): Result<ByteArray> {
        return try {
            val result = clients.firstOrNull { it.isMaintain(input.input.assetId.chain) }?.signTransfer(
                    params = input,
                    privateKey = privateKey
                ) ?: throw Exception("Impossible sign transfer")
            Result.success(result)
        } catch (err: Throwable) {
            Result.failure(err)
        }
    }

    override suspend fun invoke(chain: Chain, input: ByteArray, privateKey: ByteArray): Result<ByteArray> {
        return try {
            val result = clients.firstOrNull { it.isMaintain(chain) }?.signMessage(
                input = input,
                privateKey = privateKey
            ) ?: throw Exception("Impossible sign transfer")
            Result.success(result)
        } catch (err: Throwable) {
            Result.failure(err)
        }
    }
}