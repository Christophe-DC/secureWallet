package com.cdcoding.network.client.xrp

import com.cdcoding.model.Chain
import com.cdcoding.model.TransactionChages
import com.cdcoding.model.TransactionState
import com.cdcoding.network.client.TransactionStatusClient
import com.cdcoding.network.util.getOrNull


class XrpTransactionStatusClient(
    private val chain: Chain,
    private val apiClient: XrpApiClient,
) : TransactionStatusClient {
    override suspend fun getStatus(owner: String, txId: String): Result<TransactionChages> {
        val transaction = apiClient.transaction(txId).getOrNull() ?: return Result.failure(Exception("Transaction not found"))
         return Result.success(
             TransactionChages(
                if (transaction.result.status == "success") TransactionState.Confirmed else TransactionState.Pending
            )
         )
    }

    override fun maintainChain(): Chain = chain
}