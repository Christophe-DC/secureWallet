package com.cdcoding.data.local.di

import com.cdcoding.data.local.db.DatabaseDriverFactory
import com.cdcoding.data.local.db.IOSDatabaseDriverFactory
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module {
    return module {
        single<DatabaseDriverFactory> { IOSDatabaseDriverFactory() }
    }
}