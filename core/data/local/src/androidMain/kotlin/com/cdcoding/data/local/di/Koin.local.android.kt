package com.cdcoding.data.local.di


import com.cdcoding.data.local.db.AndroidDatabaseDriverFactory
import com.cdcoding.data.local.db.DatabaseDriverFactory
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module {
    return module {
        single<DatabaseDriverFactory> { AndroidDatabaseDriverFactory(get()) }
    }
}