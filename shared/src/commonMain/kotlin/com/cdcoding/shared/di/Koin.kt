package com.cdcoding.shared.di

import com.cdcoding.data.di.dataModule
import com.cdcoding.database.di.databaseModule
import com.cdcoding.domain.di.useCaseDomainModule
import com.cdcoding.network.di.networkModule
import com.cdcoding.wallet.di.walletModule
import com.cdcoding.datastore.di.dataStoreModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration


fun initKoin(declaration: KoinAppDeclaration = {}) =
    startKoin {
        declaration()

        modules(
            dataModule,
            databaseModule,
            networkModule,
            useCaseDomainModule,
            viewModelModule,
            walletModule,
            dataStoreModule,
        )
    }

fun initKoin() = initKoin {}
