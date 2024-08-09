package com.cdcoding.database.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.cdcoding.local.db.SecureWalletDatabase

class IOSDatabaseDriverFactory : DatabaseDriverFactory {
    override fun createDriver(): SqlDriver {
        return NativeSqliteDriver(SecureWalletDatabase.Schema, DB_NAME).also {
            it.execute(null,"PRAGMA foreign_keys=ON",0)
        }
    }
}