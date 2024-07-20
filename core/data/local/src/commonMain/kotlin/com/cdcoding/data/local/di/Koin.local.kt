package com.cdcoding.data.local.di


import com.cdcoding.data.local.db.DefaultWalletDao
import com.cdcoding.data.local.db.WalletDao
import com.cdcoding.data.local.db.createDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

expect fun platformModule(): Module

val localModule = module {
    includes(platformModule())
    factory { createDatabase(get()) }
    single<WalletDao> { DefaultWalletDao(get()) }
}
