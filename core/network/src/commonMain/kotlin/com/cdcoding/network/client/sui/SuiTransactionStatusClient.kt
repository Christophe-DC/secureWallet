package com.cdcoding.network.client.sui

import com.cdcoding.model.Chain
import com.cdcoding.model.TransactionChages
import com.cdcoding.model.TransactionState
import com.cdcoding.network.client.TransactionStatusClient
import com.cdcoding.network.util.getOrNull


class SuiTransactionStatusClient(
    private val chain: Chain,
    private val apiClient: SuiApiClient,
) : TransactionStatusClient {
    override suspend fun getStatus(owner: String, txId: String): Result<TransactionChages> {
        val transaction = apiClient.transaction(txId).getOrNull()
            ?: return Result.failure(Exception("Transaction not found"))
        return Result.success(
            TransactionChages(
                when (transaction.result.effects.status.status) {
                    "success" -> TransactionState.Confirmed
                    "failure" -> TransactionState.Reverted
                    else -> TransactionState.Pending
                }
            )
        )
    }

    override fun maintainChain(): Chain = chain
}