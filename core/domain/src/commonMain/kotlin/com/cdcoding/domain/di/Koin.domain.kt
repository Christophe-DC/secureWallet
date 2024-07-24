package com.cdcoding.domain.di

import com.cdcoding.domain.CreateWalletUseCase
import com.cdcoding.domain.GetHasSessionUseCase
import com.cdcoding.domain.GetNextWalletNumberUseCase
import org.koin.dsl.module

val useCaseDomainModule = module {
    single<CreateWalletUseCase> { CreateWalletUseCase(get()) }
    single<GetNextWalletNumberUseCase> { GetNextWalletNumberUseCase(get()) }
    single<GetHasSessionUseCase> { GetHasSessionUseCase(get()) }
}
