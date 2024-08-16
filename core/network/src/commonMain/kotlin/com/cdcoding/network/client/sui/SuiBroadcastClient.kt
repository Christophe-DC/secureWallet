package com.cdcoding.network.client.sui

import com.cdcoding.model.Account
import com.cdcoding.model.Chain
import com.cdcoding.model.TransactionType
import com.cdcoding.network.client.BroadcastClient
import com.cdcoding.network.util.getOrNull


class SuiBroadcastClient(
    private val chain: Chain,
    private val rpcClient: SuiApiClient,
) : BroadcastClient {
    override suspend fun send(account: Account, signedMessage: ByteArray, type: TransactionType): Result<String> {
        val parts = signedMessage.toString().split("_")
        val data = parts.first()
        val sign = parts[1]
        val result = rpcClient.broadcast(data, sign).getOrNull()?.result?.digest ?: throw Exception("Unknown error")
        return Result.success(result)
    }

    override fun maintainChain(): Chain = chain

}