package com.cdcoding.network.client.cosmo

import com.cdcoding.model.Account
import com.cdcoding.model.Chain
import com.cdcoding.model.TransactionType
import com.cdcoding.network.client.BroadcastClient
import com.cdcoding.network.util.getOrNull


class CosmosBroadcastClient(
    private val chain: Chain,
    private val client: CosmosApiClient,
) : BroadcastClient {

    override suspend fun send(account: Account, signedMessage: ByteArray, type: TransactionType): Result<String> {
        val result = client.broadcast(signedMessage).getOrNull()?.tx_response?.txhash ?: throw Exception("Unknown error")
        return Result.success(result)
    }

    override fun maintainChain(): Chain = chain
}