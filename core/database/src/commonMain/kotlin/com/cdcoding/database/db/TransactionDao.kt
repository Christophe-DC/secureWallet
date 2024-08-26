package com.cdcoding.database.db


import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.cdcoding.common.utils.toIdentifier
import com.cdcoding.database.db.model.asEntity
import com.cdcoding.database.mapper.asExternal
import com.cdcoding.database.mapper.toAssetEntityId
import com.cdcoding.database.mapper.toExtendedTransaction
import com.cdcoding.local.db.SecureWalletDatabase
import com.cdcoding.model.Account
import com.cdcoding.model.Transaction
import com.cdcoding.model.TransactionExtended
import com.cdcoding.model.TransactionSwapMetadata
import com.cdcoding.model.TransactionType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json

interface TransactionDao {
    suspend fun addTransaction(transaction: Transaction)
    suspend fun putTransactions(transactions: List<Transaction>)
    suspend fun getExtendedTransactions(txIds: List<String> = emptyList()): Flow<List<TransactionExtended?>>
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

    override suspend fun putTransactions(transactions: List<Transaction>) {
        transactionQueries.transaction {
            transactions.forEach { transaction ->
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
            }
            addSwapMetadata(transactions)
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

    override suspend fun getExtendedTransactions(txIds: List<String>): Flow<List<TransactionExtended?>> {
        return transactionQueries.getExtendedTransactions(txIds).asFlow()
            .mapToList(Dispatchers.IO)
            .map { transactionEntities ->
                transactionEntities.map { transactionEntity ->
                    transactionEntity.toExtendedTransaction()
                }
            }
    }

}
