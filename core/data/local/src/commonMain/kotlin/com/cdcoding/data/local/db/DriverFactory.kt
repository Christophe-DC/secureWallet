package com.cdcoding.data.local.db

import app.cash.sqldelight.db.SqlDriver
import com.cdcoding.data.local.db.adapter.AccountChainAdapter
import com.cdcoding.data.local.db.adapter.CurrencyAdapter
import com.cdcoding.data.local.db.adapter.WalletTypeAdapter
import com.cdcoding.local.db.AccountEntity
import com.cdcoding.local.db.SecureWalletDatabase
import com.cdcoding.local.db.SessionEntity
import com.cdcoding.local.db.WalletEntity


interface DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}

fun createDatabase(driver: DatabaseDriverFactory): SecureWalletDatabase {
    return SecureWalletDatabase.invoke(driver.createDriver(),
        AccountEntity.Adapter(chainAdapter = AccountChainAdapter()),
        SessionEntity.Adapter(currencyAdapter = CurrencyAdapter()),
        WalletEntity.Adapter(typeAdapter = WalletTypeAdapter())
    )
}