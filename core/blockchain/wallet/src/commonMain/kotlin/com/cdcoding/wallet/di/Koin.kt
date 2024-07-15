package com.cdcoding.wallet.di

import com.cdcoding.wallet.WalletClient
import org.koin.dsl.module


val walletModule = module {
    factory<WalletClient> { WalletClient() }
}
