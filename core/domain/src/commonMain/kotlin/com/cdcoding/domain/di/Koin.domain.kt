package com.cdcoding.domain.di

import com.cdcoding.domain.GetCreateWalletUseCase
import org.koin.dsl.module

val useCaseDomainModule = module {
    single<GetCreateWalletUseCase> { GetCreateWalletUseCase(get()) }
}
