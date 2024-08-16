package com.cdcoding.database.db


import com.cdcoding.common.utils.toIdentifier
import com.cdcoding.database.db.model.asEntity
import com.cdcoding.database.mapper.toAssetEntityId
import com.cdcoding.local.db.SecureWalletDatabase
import com.cdcoding.model.Transaction
import com.cdcoding.model.TransactionSwapMetadata
import com.cdcoding.model.TransactionType
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json

interface TransactionDao {
    suspend fun addTransaction(transaction: Transaction)
}

class DefaultTransactionDao(
    database: SecureWalletDatabase
) : TransactionDao {

    private val transactionQueries = database.transactionQueries


    override suspend fun addTransaction(transaction: Transaction) {
            transactionQueries.transaction {
                transactionQueries.insertTransaction(
                    id = transaction.id,
                    hash = transaction.hash,
                    assetId = transaction.assetId.toAssetEntityId(),
                    feeAssetId = transaction.feeAssetId.toAssetEntityId(),
                    owner = transaction.from,
                    recipient = transaction.to,
                    contract = transaction.contract,
                    metadata = transaction.metadata,
                    state = transaction.state.asEntity(),
                    type = transaction.type.asEntity(),
                    blockNumber = transaction.blockNumber,
                    sequence = transaction.sequence,
                    fee = transaction.fee,
                    value_ = transaction.value,
                    payload = transaction.memo,
                    direction = transaction.direction.asEntity(),
                    createdAt = Clock.System.now().epochSeconds,
                    updatedAt = Clock.System.now().epochSeconds
                )
                addSwapMetadata(listOf(transaction))
            }
        }

    private fun addSwapMetadata(txs: List<Transaction>) {
        txs.filter { it.type == TransactionType.Swap && it.metadata != null }.forEach {
            val txMetadata = Json.decodeFromString<TransactionSwapMetadata>(it.metadata!!)
            transactionQueries.insertSwapMetadata(
                txId = it.id,
                fromAssetId = txMetadata.fromAsset.toIdentifier(),
                toAssetId = txMetadata.toAsset.toIdentifier(),
                fromAmount = txMetadata.fromValue,
                toAmount = txMetadata.toValue,
            )
        }
    }

}
