package com.cdcoding.securewallet.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration


fun initKoin(declaration: KoinAppDeclaration = {}) =
    startKoin {
        declaration()

        modules(
            viewModelModule
        )
    }

fun initKoin() = initKoin {}
