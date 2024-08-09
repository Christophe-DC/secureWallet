package com.cdcoding.database.di

import com.cdcoding.database.db.DatabaseDriverFactory
import com.cdcoding.database.db.DesktopDatabaseDriverFactory
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module {
    return module {
        single<DatabaseDriverFactory> { DesktopDatabaseDriverFactory() }
    }
}