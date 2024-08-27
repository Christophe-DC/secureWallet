package com.cdcoding.network.client.bitcoin

import com.cdcoding.model.Account
import com.cdcoding.model.Chain
import com.cdcoding.model.TransactionType
import com.cdcoding.network.client.BroadcastClient
import com.cdcoding.network.model.RpcError
import com.cdcoding.network.util.getOrNull


class BitcoinBroadcastClient(
    private val chain: Chain,
    private val apiClient: BitcoinApiClient,
) : BroadcastClient {

    override suspend fun send(account: Account, signedMessage: ByteArray, type: TransactionType): Result<String> {
        val result = apiClient.broadcast(signedMessage).getOrNull()?.result ?: throw RpcError.BroadcastFail("Unknown error")
        return Result.success(result)
    }

    override fun maintainChain(): Chain = chain
}