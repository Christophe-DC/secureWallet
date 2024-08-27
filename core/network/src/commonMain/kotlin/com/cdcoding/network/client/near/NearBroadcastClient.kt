package com.cdcoding.network.client.near

import com.cdcoding.model.Account
import com.cdcoding.model.Chain
import com.cdcoding.model.TransactionType
import com.cdcoding.network.client.BroadcastClient
import com.cdcoding.network.model.JSONRpcRequest
import com.cdcoding.network.util.getOrNull


class NearBroadcastClient(
    private val chain: Chain,
    private val apiClient: NearApiClient,
) : BroadcastClient {
    override suspend fun send(
        account: Account,
        signedMessage: ByteArray,
        type: TransactionType
    ): Result<String> {
        val request = apiClient.broadcast(
            JSONRpcRequest(
                NearMethod.Broadcast.value,
                params = mapOf(
                    "signed_tx_base64" to signedMessage.toString()
                )
            )
        ).getOrNull()?.result?.transaction?.hash ?: throw Exception("Unknown error")
        return Result.success(request)
    }

    override fun maintainChain(): Chain = chain
}