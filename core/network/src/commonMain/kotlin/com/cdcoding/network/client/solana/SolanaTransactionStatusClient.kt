package com.cdcoding.network.client.solana

import com.cdcoding.model.Chain
import com.cdcoding.model.TransactionChages
import com.cdcoding.model.TransactionState
import com.cdcoding.network.client.TransactionStatusClient
import com.cdcoding.network.model.JSONRpcRequest
import com.cdcoding.network.util.getOrNull


class SolanaTransactionStatusClient(
    private val apiClient: SolanaApiClient,
) : TransactionStatusClient {

    override suspend fun getStatus(owner: String, txId: String): Result<TransactionChages> {
        val request = JSONRpcRequest(
            SolanaMethod.GetTransaction.value,
            listOf(
                txId,
                mapOf(
                    "encoding" to "jsonParsed",
                    "maxSupportedTransactionVersion" to 0,
                ),
            )
        )
        val transaction = apiClient.transaction(request).getOrNull() ?: return Result.success(TransactionChages(TransactionState.Failed))
        if (transaction.error != null) return Result.success(TransactionChages(TransactionState.Failed))
        val state = if (transaction.result.slot > 0) {
            if (transaction.result.meta.err != null) {
                TransactionState.Failed
            } else {
                TransactionState.Confirmed
            }
        } else {
            TransactionState.Pending
        }
        return Result.success(TransactionChages(state))

    }

    override fun maintainChain(): Chain = Chain.Solana
}