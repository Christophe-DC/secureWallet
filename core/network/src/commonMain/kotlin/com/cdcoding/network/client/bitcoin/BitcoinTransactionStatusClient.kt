package com.cdcoding.network.client.bitcoin

import com.cdcoding.model.Chain
import com.cdcoding.model.TransactionChages
import com.cdcoding.model.TransactionState
import com.cdcoding.network.client.TransactionStatusClient
import com.cdcoding.network.util.getOrNull

class BitcoinTransactionStatusClient(
    private val chain: Chain,
    private val apiClient: BitcoinApiClient,
) : TransactionStatusClient {
    override suspend fun getStatus(owner: String, txId: String): Result<TransactionChages> {
        val transaction = apiClient.transaction(txId).getOrNull()
            ?: return Result.failure(Exception("Transaction not found"))
        return Result.success(
            TransactionChages(
                if (transaction.blockHeight > 0) TransactionState.Confirmed else TransactionState.Pending
            )
        )
    }

    override fun maintainChain(): Chain = chain
}