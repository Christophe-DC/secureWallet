package com.cdcoding.network.client.ethereum

import com.cdcoding.model.Account
import com.cdcoding.model.Chain
import com.cdcoding.model.TransactionType
import com.cdcoding.network.client.BroadcastClient
import com.cdcoding.network.model.JSONRpcRequest
import com.cdcoding.network.util.getOrNull


class EvmBroadcastClient(
    private val chain: Chain,
    private val client: EvmApiClient,
) : BroadcastClient {
    @OptIn(ExperimentalStdlibApi::class)
    override suspend fun send(account: Account, signedMessage: ByteArray, type: TransactionType): Result<String> {
        val request = JSONRpcRequest.create(EvmMethod.Broadcast, listOf(signedMessage.toHexString()))
        val result =client.broadcast(request).getOrNull()?.result ?: throw Exception("Unknown error")
        return Result.success(result)
    }

    override fun maintainChain(): Chain = chain
}