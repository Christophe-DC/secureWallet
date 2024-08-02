package com.cdcoding.wallet.di

import com.cdcoding.wallet.client.WalletClient
import org.koin.core.module.Module
import org.koin.dsl.module

expect fun platformModule(): Module

val walletModule = module {
    includes(platformModule())
    factory<WalletClient> { WalletClient() }
}
