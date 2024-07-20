package com.cdcoding.data.local.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.cdcoding.local.db.SecureWalletDatabase
import com.cdcoding.data.local.db.DatabaseDriverFactory

class IOSDatabaseDriverFactory : DatabaseDriverFactory {
    override fun createDriver(): SqlDriver {
        return NativeSqliteDriver(SecureWalletDatabase.Schema, "launch.db").also {
            it.execute(null,"PRAGMA foreign_keys=ON",0)
        }
    }
}