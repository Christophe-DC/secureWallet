package com.cdcoding.securewallet.di

import com.cdcoding.data.local.di.localModule
import com.cdcoding.domain.di.useCaseDomainModule
import com.cdcoding.wallet.di.walletModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration


fun initKoin(declaration: KoinAppDeclaration = {}) =
    startKoin {
        declaration()

        modules(
            viewModelModule,
            walletModule,
            useCaseDomainModule,
            localModule
        )
    }

fun initKoin() = initKoin {}
