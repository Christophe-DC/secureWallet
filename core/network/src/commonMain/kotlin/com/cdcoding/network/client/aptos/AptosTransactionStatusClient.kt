package com.cdcoding.network.client.aptos

import com.cdcoding.model.Chain
import com.cdcoding.model.TransactionChages
import com.cdcoding.model.TransactionState
import com.cdcoding.network.client.TransactionStatusClient
import com.cdcoding.network.util.getOrNull


class AptosTransactionStatusClient(
    private val chain: Chain,
    private val apiClient: AptosApiClient,
) : TransactionStatusClient {
    override suspend fun getStatus(owner: String, txId: String): Result<TransactionChages> {
        val status = if (apiClient.transactions(txId).getOrNull()?.success == true) {
            TransactionState.Confirmed
        } else {
            TransactionState.Pending
        }
        return Result.success(TransactionChages(status))
    }

    override fun maintainChain(): Chain = chain
}