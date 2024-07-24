package com.cdcoding.data.local.db


import com.cdcoding.data.local.db.model.asEntity
import com.cdcoding.data.local.mapper.asExternal
import com.cdcoding.local.db.SecureWalletDatabase
import com.cdcoding.model.Account

interface AccountDao {
    fun getAccountsByWalletId(walletId: String): List<Account>
    fun insertAccounts(accounts: List<Account>, walletId: String)
}

class DefaultAccountDao(
    database: SecureWalletDatabase
) : AccountDao {

    private val accountQueries = database.accountQueries

    override fun getAccountsByWalletId(walletId: String): List<Account> {
        return accountQueries.getAccountsByWalletId(walletId).executeAsList().map { it.asExternal() }
    }

    override fun insertAccounts(accounts: List<Account>, walletId: String) {
        accountQueries.transaction {
            accounts.forEach { account ->
                accountQueries.insertAccount(
                    id = account.id,
                    chain = account.chain.asEntity(),
                    address = account.address,
                    derivationPath = account.derivationPath,
                    extendedPublicKey = account.extendedPublicKey,
                    walletId = walletId
                )
            }
        }
    }
}
