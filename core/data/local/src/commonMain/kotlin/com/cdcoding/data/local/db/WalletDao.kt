package com.cdcoding.data.local.db

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.cdcoding.local.db.AccountEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import com.cdcoding.local.db.SecureWalletDatabase
import com.cdcoding.local.db.WalletEntity

interface WalletDao {
    fun getAllWallets(): Flow<List<WalletEntity>>
    fun insertWallet(wallet: WalletEntity)
    fun insertWalletWithAccount(wallet: WalletEntity, accounts: List<AccountEntity>)
}

class DefaultWalletDao(
    database: SecureWalletDatabase
) : WalletDao {

    private val queries = database.walletQueries
    private val accountQueries = database.accountQueries

    override fun getAllWallets(): Flow<List<WalletEntity>> {
        return queries.getAllWallets().asFlow().mapToList(Dispatchers.IO)
    }

    override fun insertWallet(wallet: WalletEntity) {
        queries.transaction {
            println("Entity is $wallet")
            queries.insertWallet(
                id = wallet.id,
                name = wallet.name,
                wallet_index = wallet.wallet_index,
                type = wallet.type
            )
        }
    }

    override fun insertWalletWithAccount(wallet: WalletEntity, accounts: List<AccountEntity>) {
        queries.transaction {
            queries.insertWallet(
                id = wallet.id,
                name = wallet.name,
                wallet_index = wallet.wallet_index,
                type = wallet.type
            )
            accounts.forEach { account ->
                accountQueries.insertAccount(
                    id = account.id,
                    chain = account.chain,
                    address = account.address,
                    derivationPath = account.derivationPath,
                    extendedPublicKey = account.extendedPublicKey,
                    wallet_Id = wallet.id
                )
            }
        }
    }
}
