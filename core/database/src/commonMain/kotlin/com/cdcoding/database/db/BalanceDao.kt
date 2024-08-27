package com.cdcoding.database.db

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.cdcoding.common.utils.toIdentifier
import com.cdcoding.database.db.model.asEntity
import com.cdcoding.database.mapper.asExternal
import com.cdcoding.local.db.SecureWalletDatabase
import com.cdcoding.model.Account
import com.cdcoding.model.AssetBalance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

interface BalanceDao {
    suspend fun setBalance(account: Account, assetBalance: AssetBalance)
    suspend fun setBalances(account: Account, assetBalances: List<AssetBalance>)
    fun getBalancesByOwner(addresses: List<String>): Flow<List<AssetBalance?>>
    fun getBalancesByAssetId(addresses: List<String>, assetId: List<String>): Flow<List<AssetBalance?>>
}

class DefaultBalanceDao(
    database: SecureWalletDatabase
) : BalanceDao {
    //todo remove all withContext??

    private val balanceQueries = database.balanceQueries

    override suspend fun setBalance(account: Account, assetBalance: AssetBalance) =
        withContext(Dispatchers.IO) {
            val updatedAt = Clock.System.now().epochSeconds
            balanceQueries.transaction {
                balanceQueries.insertBalance(
                    assetId = assetBalance.assetId.toIdentifier(),
                    address = account.address,
                    type = assetBalance.balance.type.asEntity(),
                    amount = assetBalance.balance.value,
                    updatedAt = updatedAt
                )
            }
        }

    override suspend fun setBalances(account: Account, assetBalances: List<AssetBalance>) =
        withContext(Dispatchers.IO) {
            val updatedAt = Clock.System.now().epochSeconds
            balanceQueries.transaction {
                assetBalances.forEach { assetBalance ->
                    balanceQueries.insertBalance(
                        assetId = assetBalance.assetId.toIdentifier(),
                        address = account.address,
                        type = assetBalance.balance.type.asEntity(),
                        amount = assetBalance.balance.value,
                        updatedAt = updatedAt
                    )
                }
            }
        }

    override fun getBalancesByOwner(addresses: List<String>): Flow<List<AssetBalance?>> =
        balanceQueries.getBalancesByOwner(addresses).asFlow()
            .mapToList(Dispatchers.IO)
            .map { balacneEntities ->
                balacneEntities.map { balacneEntity ->
                    balacneEntity.asExternal()
                }
            }

    override fun getBalancesByAssetId(
        addresses: List<String>,
        assetId: List<String>
    ): Flow<List<AssetBalance?>> =
        balanceQueries.getBalancesByAssetId(addresses, assetId).asFlow()
            .mapToList(Dispatchers.IO)
            .map { balacneEntities ->
                balacneEntities.map { balanceEntity ->
                    balanceEntity.asExternal()
                }
            }


}
