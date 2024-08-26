package com.cdcoding.network.client.ethereum

import com.cdcoding.common.utils.eip1559Support
import com.cdcoding.common.utils.hexToBigInteger
import com.cdcoding.model.Chain
import com.cdcoding.model.TransactionChages
import com.cdcoding.model.TransactionState
import com.cdcoding.network.client.TransactionStatusClient
import com.cdcoding.network.model.JSONRpcRequest
import com.cdcoding.network.util.fold
import com.cdcoding.network.util.getOrNull
import com.ionspin.kotlin.bignum.integer.BigInteger


class EvmTransactionStatusClient(
    private val chain: Chain,
    private val apiClient: EvmApiClient,
) : TransactionStatusClient {
    override suspend fun getStatus(owner: String, txId: String): Result<TransactionChages> {
        val transactionByHash = apiClient.getTransactionByHash(
            JSONRpcRequest.create(
                EvmMethod.GetTransactionByHash,
                listOf(txId)
            )
        ).getOrNull()
        if (transactionByHash?.result == null) {
            return Result.success(TransactionChages(TransactionState.Failed))
        }
        return if ((transactionByHash.result.blockNumber.hexToBigInteger()
                ?: BigInteger.ZERO) <= BigInteger.ZERO
        ) {
            Result.success(TransactionChages(TransactionState.Pending))
        } else {
            Result.success(getStatus(txId))
        }
    }

    private suspend fun getStatus(txId: String): TransactionChages {
        return apiClient.transaction(JSONRpcRequest.create(EvmMethod.GetTransaction, listOf(txId)))
            .fold(
                {
                    val state = when (it.result?.status) {
                        "0x0" -> TransactionState.Reverted
                        "0x1" -> TransactionState.Confirmed
                        else -> TransactionState.Pending
                    }
                    val fee = if (chain.eip1559Support()) {
                        val gasUsed =
                            it.result?.gasUsed?.hexToBigInteger() ?: return@fold TransactionChages(
                                TransactionState.Pending
                            )
                        val effectiveGas = it.result.effectiveGasPrice.hexToBigInteger()
                            ?: return@fold TransactionChages(TransactionState.Pending)
                        val l1Fee = it.result.l1Fee?.hexToBigInteger() ?: BigInteger.ZERO
                        gasUsed.multiply(effectiveGas) + l1Fee
                    } else {
                        null
                    }

                    TransactionChages(state, fee)
                }
            ) { TransactionChages(TransactionState.Pending) }
    }

    override fun maintainChain(): Chain = chain
}