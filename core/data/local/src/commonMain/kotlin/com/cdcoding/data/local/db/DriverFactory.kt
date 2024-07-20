package com.cdcoding.data.local.db

import app.cash.sqldelight.db.SqlDriver
import com.cdcoding.local.db.SecureWalletDatabase


interface DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}

fun createDatabase(driver: DatabaseDriverFactory): SecureWalletDatabase {
    return SecureWalletDatabase.invoke(driver.createDriver())
}