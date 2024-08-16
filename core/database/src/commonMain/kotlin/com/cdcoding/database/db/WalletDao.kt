package com.cdcoding.database.db

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.cdcoding.database.db.model.asEntity
import com.cdcoding.database.mapper.asExternal
import com.cdcoding.local.db.SecureWalletDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import com.cdcoding.model.Account
import com.cdcoding.model.Wallet
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.map

interface WalletDao {
    fun getAllWallet(id: String): Wallet
    fun getAllWallets(): Flow<List<Wallet>>
    fun insertWallet(wallet: Wallet)
    fun insertWalletWithAccount(wallet: Wallet, accounts: List<Account>)
    suspend fun removeWallet(walletId: String)
}

class DefaultWalletDao(
    database: SecureWalletDatabase,
    private val accountDao: AccountDao
) : WalletDao {

    private val walletQueries = database.walletQueries

    override fun getAllWallet(id: String): Wallet {
        val wallet = walletQueries.getAllWallet(id).executeAsList().first()
        val accounts = accountDao.getAccountsByWalletId(wallet.id)
        return wallet.asExternal(accounts)
    }

    override fun getAllWallets(): Flow<List<Wallet>> {
        return walletQueries.getAllWallets().asFlow().mapToList(Dispatchers.IO)
            .map { walletEntities ->
                walletEntities.map { walletEntity ->
                    val accounts = accountDao.getAccountsByWalletId(walletEntity.id)
                    walletEntity.asExternal(accounts)
                }
            }
    }

    override fun insertWallet(wallet: Wallet) {
        walletQueries.transaction {
            walletQueries.insertWallet(
                id = wallet.id,
                name = wallet.name,
                walletIndex = wallet.index,
                type = wallet.type.asEntity()
            )
        }
    }

    override fun insertWalletWithAccount(wallet: Wallet, accounts: List<Account>) {
        walletQueries.transaction {
            walletQueries.insertWallet(
                id = wallet.id,
                name = wallet.name,
                walletIndex = wallet.index,
                type = wallet.type.asEntity()
            )
            accountDao.insertAccounts(
                accounts = accounts,
                walletId = wallet.id
            )
        }
    }


    override suspend fun removeWallet(walletId: String) {
        walletQueries.transaction {
            walletQueries.deleteWallet(walletId)
        }
    }
}
