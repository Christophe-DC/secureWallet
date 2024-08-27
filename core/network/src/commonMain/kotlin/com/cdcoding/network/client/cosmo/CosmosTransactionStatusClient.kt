package com.cdcoding.network.client.cosmo

import com.cdcoding.model.Chain
import com.cdcoding.model.TransactionChages
import com.cdcoding.model.TransactionState
import com.cdcoding.network.client.TransactionStatusClient
import com.cdcoding.network.util.getOrNull


class CosmosTransactionStatusClient(
    private val chain: Chain,
    private val apiClient: CosmosApiClient,
) : TransactionStatusClient {
    override suspend fun getStatus(owner: String, txId: String): Result<TransactionChages> {

        val transaction = apiClient.transaction(txId).getOrNull()
        return Result.success(
            TransactionChages(
                if (transaction?.tx_response == null) {
                    TransactionState.Reverted
                } else if (transaction.tx_response.txhash.isEmpty()) {
                    TransactionState.Pending
                } else if (transaction.tx_response.code == 0) {
                    TransactionState.Confirmed
                } else {
                    TransactionState.Reverted
                }
            )
        )
    }

    override fun maintainChain(): Chain = chain
}