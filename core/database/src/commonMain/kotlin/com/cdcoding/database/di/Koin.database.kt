package com.cdcoding.database.di


import com.cdcoding.database.db.AccountDao
import com.cdcoding.database.db.AssetDao
import com.cdcoding.database.db.BalanceDao
import com.cdcoding.database.db.DefaultAccountDao
import com.cdcoding.database.db.DefaultAssetDao
import com.cdcoding.database.db.DefaultBalanceDao
import com.cdcoding.database.db.DefaultPriceDao
import com.cdcoding.database.db.DefaultSessionDao
import com.cdcoding.database.db.DefaultTokenDao
import com.cdcoding.database.db.DefaultTransactionDao
import com.cdcoding.database.db.DefaultWalletDao
import com.cdcoding.database.db.PriceDao
import com.cdcoding.database.db.SessionDao
import com.cdcoding.database.db.TokenDao
import com.cdcoding.database.db.TransactionDao
import com.cdcoding.database.db.WalletDao
import com.cdcoding.database.db.createDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

expect fun platformModule(): Module

val databaseModule = module {
    includes(platformModule())
    single { createDatabase(get()) }
    single<AccountDao> { DefaultAccountDao(get()) }
    single<WalletDao> { DefaultWalletDao(get(), get()) }
    single<SessionDao> { DefaultSessionDao(get(), get()) }
    single<AssetDao> { DefaultAssetDao(get()) }
    single<BalanceDao> { DefaultBalanceDao(get()) }
    single<PriceDao> { DefaultPriceDao(get()) }
    single<TokenDao> { DefaultTokenDao(get()) }
    single<TransactionDao> { DefaultTransactionDao(get()) }
}
