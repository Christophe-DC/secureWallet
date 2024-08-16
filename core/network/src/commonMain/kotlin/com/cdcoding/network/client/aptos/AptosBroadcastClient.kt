package com.cdcoding.network.client.aptos

import com.cdcoding.model.Account
import com.cdcoding.model.Chain
import com.cdcoding.model.TransactionType
import com.cdcoding.network.client.BroadcastClient
import com.cdcoding.network.util.getOrNull


class AptosBroadcastClient(
    private val chain: Chain,
    private val apiClient: AptosApiClient,
) : BroadcastClient {
    override suspend fun send(
        account: Account,
        signedMessage: ByteArray,
        type: TransactionType
    ): Result<String> = try {
        val hash = apiClient.broadcast(signedMessage).getOrNull()?.hash
        if (hash != null)
            Result.success(hash)
        else
            Result.failure(Exception("Unknown error"))
    } catch (err: Throwable) {
        Result.failure(err)
    }

    override fun maintainChain(): Chain = chain
}