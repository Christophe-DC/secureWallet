package com.cdcoding.network.client.near

import com.cdcoding.model.Chain
import com.cdcoding.model.TransactionChages
import com.cdcoding.model.TransactionState
import com.cdcoding.network.client.TransactionStatusClient
import com.cdcoding.network.model.JSONRpcRequest
import com.cdcoding.network.util.getOrNull


class NearTransactionStatusClient(
    private val chain: Chain,
    private val apiClient: NearApiClient,
) : TransactionStatusClient {
    override suspend fun getStatus(owner: String, txId: String): Result<TransactionChages> {
        val transactionByHash = apiClient.transaction(
            JSONRpcRequest(
                method = NearMethod.Transaction.value,
                params = mapOf(
                    "tx_hash" to txId,
                    "sender_account_id" to owner,
                    "wait_until" to "EXECUTED",
                ),
            )
        ).getOrNull()
        if (transactionByHash?.error != null) {
            throw IllegalStateException(transactionByHash.error.message)
        }
        return Result.success(
            when (transactionByHash?.result?.final_execution_status) {
                "FINAL" -> TransactionChages(state = TransactionState.Confirmed)
                else -> TransactionChages(state = TransactionState.Confirmed)
            }
        )
    }

    override fun maintainChain(): Chain = chain
}