package com.cdcoding.network.client.solana

import com.cdcoding.model.Account
import com.cdcoding.model.Chain
import com.cdcoding.model.TransactionType
import com.cdcoding.network.client.BroadcastClient
import com.cdcoding.network.model.JSONRpcRequest
import com.cdcoding.network.util.getOrNull


class SolanaBroadcastClient(
    private val rpcClient: SolanaApiClient,
) : BroadcastClient {
    override suspend fun send(account: Account, signedMessage: ByteArray, type: TransactionType): Result<String> {
        val encodedMessage = signedMessage.toString()
        val params = if (type == TransactionType.Swap) {
            listOf(
                encodedMessage,
                mapOf(
                    "encoding" to "base64",
                    "skipPreflight" to true,
                ),
            )
        } else {
            listOf(
                encodedMessage,
                mapOf("encoding" to "base64"),
            )
        }
        val request = JSONRpcRequest.create(SolanaMethod.SendTransaction, params)
        val result = rpcClient.broadcast(request).getOrNull()?.result ?: throw Exception("Unknown error")
        return Result.success(result)
    }

    override fun maintainChain(): Chain = Chain.Solana
}