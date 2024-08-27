package com.cdcoding.datastore.di

import com.cdcoding.datastore.ConfigRepository
import com.cdcoding.datastore.OfflineFirstConfigRepository
import org.koin.core.module.Module
import org.koin.dsl.module

expect fun platformModule(): Module

val dataStoreModule = module {
    includes(platformModule())
    single<ConfigRepository> { OfflineFirstConfigRepository(get()) }
}
