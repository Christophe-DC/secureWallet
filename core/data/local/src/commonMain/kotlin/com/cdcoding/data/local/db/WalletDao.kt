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
    fun getAllWallets(): Flow<List<Wallet>>
    fun insertWallet(wallet: Wallet)
    fun insertWalletWithAccount(wallet: Wallet, accounts: List<Account>)
}

class DefaultWalletDao(
    database: SecureWalletDatabase
) : WalletDao {

    private val walletQueries = database.walletQueries
    private val accountQueries = database.accountQueries

    override fun getAllWallets(): Flow<List<Wallet>> {
        return walletQueries.getAllWallets().asFlow().mapToList(Dispatchers.IO).map{ walletEntities ->
            walletEntities.map { walletEntity ->
                val accounts = accountQueries.getAccountsByWalletId(walletEntity.id).executeAsList()
                walletEntity.asExternal(accounts.map { it.asExternal() })
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
            accounts.forEach { account ->
                accountQueries.insertAccount(
                    id = account.id,
                    chain = account.chain.asEntity(),
                    address = account.address,
                    derivationPath = account.derivationPath,
                    extendedPublicKey = account.extendedPublicKey,
                    walletId = wallet.id
                )
            }
        }
    }
}
