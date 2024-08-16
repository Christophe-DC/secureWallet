package com.cdcoding.network.client.ton

import com.cdcoding.model.Account
import com.cdcoding.model.Chain
import com.cdcoding.model.TransactionType
import com.cdcoding.network.client.BroadcastClient
import com.cdcoding.network.util.getOrNull

class TonBroadcastClient(
    private val apiClient: TonApiClient,
) : BroadcastClient {
    override suspend fun send(account: Account, signedMessage: ByteArray, type: TransactionType): Result<String> {
        val encodedMessage = signedMessage.toString()
        val result = apiClient.broadcast(TonApiClient.Boc(encodedMessage)).getOrNull()?.result?.hash ?: throw Exception("Unknown error")
        return Result.success(result)
    }

    override fun maintainChain(): Chain = Chain.Ton
}