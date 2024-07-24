package com.cdcoding.data.local.di


import com.cdcoding.data.local.db.AccountDao
import com.cdcoding.data.local.db.DefaultAccountDao
import com.cdcoding.data.local.db.DefaultSessionDao
import com.cdcoding.data.local.db.DefaultWalletDao
import com.cdcoding.data.local.db.SessionDao
import com.cdcoding.data.local.db.WalletDao
import com.cdcoding.data.local.db.createDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

expect fun platformModule(): Module

val localModule = module {
    includes(platformModule())
    factory { createDatabase(get()) }
    single<AccountDao> { DefaultAccountDao(get()) }
    single<WalletDao> { DefaultWalletDao(get(), get()) }
    single<SessionDao> { DefaultSessionDao(get(), get()) }
}
