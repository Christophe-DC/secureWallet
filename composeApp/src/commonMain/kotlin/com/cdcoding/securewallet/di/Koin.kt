package com.cdcoding.securewallet.di

import com.cdcoding.wallet.di.walletModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration


fun initKoin(declaration: KoinAppDeclaration = {}) =
    startKoin {
        declaration()

        modules(
            viewModelModule,
            walletModule
        )
    }

fun initKoin() = initKoin {}
