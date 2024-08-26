package com.cdcoding.data.repository

import com.cdcoding.common.utils.getSwapMetadata
import com.cdcoding.common.utils.toIdentifier
import com.cdcoding.database.db.AssetDao
import com.cdcoding.database.db.TransactionDao
import com.cdcoding.model.Account
import com.cdcoding.model.Asset
import com.cdcoding.model.AssetId
import com.cdcoding.model.AssetInfo
import com.cdcoding.model.Balances
import com.cdcoding.model.Currency
import com.cdcoding.model.Fee
import com.cdcoding.model.Transaction
import com.cdcoding.model.TransactionDirection
import com.cdcoding.model.TransactionExtended
import com.cdcoding.model.TransactionState
import com.cdcoding.model.TransactionType
import com.cdcoding.model.Wallet
import com.cdcoding.network.client.TransactionStatusClient
import com.ionspin.kotlin.bignum.integer.BigInteger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

interface TransactionRepository {
    suspend fun addTransaction(
        hash: String,
        assetId: AssetId,
        owner: Account,
        to: String,
        state: TransactionState,
        fee: Fee,
        amount: BigInteger,
        memo: String?,
        type: TransactionType,
        metadata: String? = null,
        direction: TransactionDirection,
    ): Result<Transaction>

    suspend fun putTransactions(transactions: List<Transaction>)
    suspend fun getTransactions(assetId: AssetId? = null, vararg accounts: Account): Flow<List<TransactionExtended?>>
}


class DefaultTransactionsRepository(
    private val transactionDao: TransactionDao,
    private val assetDao: AssetDao,
): TransactionRepository {


    override suspend fun addTransaction(
        hash: String,
        assetId: AssetId,
        owner: Account,
        to: String,
        state: TransactionState,
        fee: Fee,
        amount: BigInteger,
        memo: String?,
        type: TransactionType,
        metadata: String?,
        direction: TransactionDirection,
    ): Result<Transaction> = withContext(Dispatchers.IO) {
        val transaction = Transaction(
            id = "${assetId.chain.string}_$hash",
            hash = hash,
            assetId = assetId,
            feeAssetId = fee.feeAssetId,
            from = owner.address,
            to = to,
            type = type,
            state = state,
            blockNumber = "",
            sequence = "", // Nonce
            fee = fee.amount.toString(),
            value = amount.toString(),
            memo = if (type == TransactionType.Swap) "" else memo,
            direction = direction,
            metadata = metadata,
            utxoInputs = emptyList(),
            utxoOutputs = emptyList(),
            createdAt =  Clock.System.now().epochSeconds,
        )
        transactionDao.addTransaction(transaction)
        Result.success(transaction)
    }

    override suspend fun putTransactions(transactions: List<Transaction>) = withContext(Dispatchers.IO) {
        transactionDao.putTransactions(transactions)
    }



    override suspend fun getTransactions(assetId: AssetId?, vararg accounts: Account): Flow<List<TransactionExtended?>> = withContext(Dispatchers.IO) {
        val chains = accounts.map { it.chain }
        transactionDao.getExtendedTransactions(emptyList())
            .map { list ->
                list.filter {
                    chains.contains(it?.asset?.id?.chain) &&
                            (assetId == null || it?.asset?.id?.toIdentifier() == assetId.toIdentifier())
                }.map {
                    val metadata = it?.transaction?.getSwapMetadata()
                    if (metadata != null) {
                        it.copy(
                            assets = listOf(
                                assetDao.getAssetsById(metadata.fromAsset.toIdentifier()).firstOrNull(),
                                assetDao.getAssetsById(metadata.toAsset.toIdentifier()).firstOrNull(),
                            ).mapNotNull { asset -> asset }
                        )
                    } else {
                        it
                    }
                }
            }
    }

}