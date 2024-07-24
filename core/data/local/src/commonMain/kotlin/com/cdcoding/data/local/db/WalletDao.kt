package com.cdcoding.data.local.db

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.cdcoding.data.local.db.model.asEntity
import com.cdcoding.data.local.mapper.asExternal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import com.cdcoding.local.db.SecureWalletDatabase
import com.cdcoding.model.Account
import com.cdcoding.model.Wallet
import kotlinx.coroutines.flow.map

interface WalletDao {
    fun getAllWallet(id: String): Wallet
    fun getAllWallets(): Flow<List<Wallet>>
    fun insertWallet(wallet: Wallet)
    fun insertWalletWithAccount(wallet: Wallet, accounts: List<Account>)
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
            println("Entity is $wallet")
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
}
