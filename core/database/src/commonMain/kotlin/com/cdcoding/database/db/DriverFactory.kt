package com.cdcoding.database.db

import app.cash.sqldelight.ColumnAdapter
import app.cash.sqldelight.db.SqlDriver
import com.cdcoding.database.db.adapter.AccountChainAdapter
import com.cdcoding.database.db.adapter.AssetTypeAdapter
import com.cdcoding.database.db.adapter.BalanceTypeAdapter
import com.cdcoding.database.db.adapter.CurrencyAdapter
import com.cdcoding.database.db.adapter.TransactionDirectionAdapter
import com.cdcoding.database.db.adapter.TransactionStateAdapter
import com.cdcoding.database.db.adapter.TransactionTypeAdapter
import com.cdcoding.database.db.adapter.WalletTypeAdapter
import com.cdcoding.database.db.model.TransactionDirectionEntity
import com.cdcoding.local.db.AccountEntity
import com.cdcoding.local.db.AssetEntity
import com.cdcoding.local.db.BalanceEntity
import com.cdcoding.local.db.SecureWalletDatabase
import com.cdcoding.local.db.SessionEntity
import com.cdcoding.local.db.TokenEntity
import com.cdcoding.local.db.TransactionEntity
import com.cdcoding.local.db.WalletEntity

val DB_NAME = "securewallet.db"

interface DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}

fun createDatabase(driver: DatabaseDriverFactory): SecureWalletDatabase {
    return SecureWalletDatabase.invoke(driver.createDriver(),
        AccountEntity.Adapter(chainAdapter = AccountChainAdapter()),
        AssetEntity.Adapter(typeAdapter = AssetTypeAdapter()),
        BalanceEntity.Adapter(typeAdapter = BalanceTypeAdapter()),
        SessionEntity.Adapter(currencyAdapter = CurrencyAdapter()),
        TokenEntity.Adapter(typeAdapter = AssetTypeAdapter()),
        TransactionEntity.Adapter(
            stateAdapter = TransactionStateAdapter(),
            typeAdapter = TransactionTypeAdapter(),
            directionAdapter = TransactionDirectionAdapter()
        ),
        WalletEntity.Adapter(typeAdapter = WalletTypeAdapter())
    )
}