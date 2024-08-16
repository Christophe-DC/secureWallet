package com.cdcoding.network.client.xrp

import com.cdcoding.common.utils.toHexString
import com.cdcoding.model.Account
import com.cdcoding.model.Chain
import com.cdcoding.model.TransactionType
import com.cdcoding.network.client.BroadcastClient
import com.cdcoding.network.util.getOrNull


class XrpBroadcastClient(
    private val chain: Chain,
    private val apiClient: XrpApiClient,
) : BroadcastClient {
    override suspend fun send(
        account: Account,
        signedMessage: ByteArray,
        type: TransactionType
    ): Result<String> {
        val response = apiClient.broadcast(signedMessage.toHexString("")).getOrNull()
            ?: throw Exception("Unknown error")
        if (!response.result.accepted && !response.result.engine_result_message.isNullOrEmpty()) {
            throw Exception(response.result.engine_result_message)
        }
        if (response.result.tx_json?.hash.isNullOrEmpty()) {
            throw Exception("Unable to get hash")
        }
        return Result.success(response.result.tx_json?.hash!!)
    }

    override fun maintainChain(): Chain = chain

}