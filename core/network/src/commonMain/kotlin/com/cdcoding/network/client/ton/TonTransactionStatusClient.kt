package com.cdcoding.network.client.ton

import com.cdcoding.model.Chain
import com.cdcoding.model.HashChanges
import com.cdcoding.model.TransactionChages
import com.cdcoding.model.TransactionState
import com.cdcoding.network.client.TransactionStatusClient
import com.cdcoding.network.util.getOrNull
import com.trustwallet.core.Base64


class TonTransactionStatusClient(
    private val apiClient: TonApiClient,
) : TransactionStatusClient {
    @OptIn(ExperimentalStdlibApi::class)
    override suspend fun getStatus(owner: String, txId: String): Result<TransactionChages> {
        val txHashData = Base64.decode(txId)
        val transaction = apiClient.transaction(txHashData?.toHexString() ?: "").getOrNull()
        val rawHash = transaction?.firstOrNull()?.hash ?: return Result.success(
            TransactionChages(
                TransactionState.Pending
            )
        )
        val newId = Base64.decode(rawHash)?.toHexString() ?: ""
        return Result.success(
            TransactionChages(
                TransactionState.Confirmed,
                hashChanges = HashChanges(old = txId, new = newId)
            )
        )
    }

    override fun maintainChain(): Chain = Chain.Ton
}