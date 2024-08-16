package com.cdcoding.data.di

import com.cdcoding.data.repository.AssetRepository
import com.cdcoding.data.repository.DefaultAssetRepository
import com.cdcoding.data.repository.DefaultSessionRepository
import com.cdcoding.data.repository.DefaultTokenRepository
import com.cdcoding.data.repository.DefaultTransactionsRepository
import com.cdcoding.data.repository.SessionRepository
import com.cdcoding.data.repository.TokenRepository
import com.cdcoding.data.repository.TransactionRepository
import com.cdcoding.data.repository.WalletRepository
import org.koin.dsl.module


val dataModule = module {
    single<SessionRepository> { DefaultSessionRepository( get()) }
    single<AssetRepository> { DefaultAssetRepository( get(), get(), get(), get(), get()) }
    single { WalletRepository(get(), get(), get(), get(), get(), get(), get(), get(), get(), get()) }
    single<TokenRepository> { DefaultTokenRepository( get(), get()) }
    single<TransactionRepository> { DefaultTransactionsRepository( get()) }
}
