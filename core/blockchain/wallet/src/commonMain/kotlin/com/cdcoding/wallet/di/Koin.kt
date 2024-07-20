package com.cdcoding.wallet.di

import com.cdcoding.wallet.client.WalletClient
import com.cdcoding.wallet.repository.WalletRepository
import org.koin.core.module.Module
import org.koin.dsl.module

expect fun platformModule(): Module

val walletModule = module {
    includes(platformModule())
    single { WalletRepository(get(), get(), get()) }
    factory<WalletClient> { WalletClient() }
}
