package com.cdcoding.network.client.tron

import com.cdcoding.model.Chain
import com.cdcoding.model.TransactionChages
import com.cdcoding.model.TransactionState
import com.cdcoding.network.client.TransactionStatusClient
import com.cdcoding.network.util.getOrNull
import com.ionspin.kotlin.bignum.integer.BigInteger


class TronTransactionStatusClient(
    private val apiClient: TronApiClient,
) : TransactionStatusClient {
    override suspend fun getStatus(owner: String, txId: String): Result<TransactionChages> {
        val transaction = apiClient.transaction(TronApiClient.TronValue(value = txId)).getOrNull()
        return Result.success(
            when {
                transaction?.receipt != null && transaction.receipt?.result == "OUT_OF_ENERGY" -> TransactionChages(
                    TransactionState.Reverted
                )

                transaction?.result == "FAILED" -> TransactionChages(TransactionState.Reverted)
                (transaction?.blockNumber ?: 0) > 0 -> {
                    val fee = transaction?.fee ?: 0
                    TransactionChages(TransactionState.Confirmed, BigInteger(fee.toLong()))
                }

                else -> TransactionChages(TransactionState.Pending)
            }
        )
    }

    override fun maintainChain(): Chain = Chain.Tron
}