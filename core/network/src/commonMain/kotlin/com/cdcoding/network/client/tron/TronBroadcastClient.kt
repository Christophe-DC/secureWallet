package com.cdcoding.network.client.tron

import com.cdcoding.model.Account
import com.cdcoding.model.Chain
import com.cdcoding.model.TransactionType
import com.cdcoding.network.client.BroadcastClient
import com.cdcoding.network.util.getOrNull


class TronBroadcastClient(
    private val apiClient: TronApiClient,
) : BroadcastClient {

    override suspend fun send(account: Account, signedMessage: ByteArray, type: TransactionType): Result<String> {
        val result = apiClient.broadcast(signedMessage).getOrNull()?.txid ?: throw Exception("Unknown error")
        return Result.success(result)
    }

    override fun maintainChain(): Chain = Chain.Tron
}