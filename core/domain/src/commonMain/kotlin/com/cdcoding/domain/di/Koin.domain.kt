package com.cdcoding.domain.di

import com.cdcoding.domain.CreateWalletUseCase
import com.cdcoding.domain.GetAssetsByWalletUseCase
import com.cdcoding.domain.GetHasSessionUseCase
import com.cdcoding.domain.GetNextWalletNumberUseCase
import com.cdcoding.domain.GetSessionUseCase
import org.koin.dsl.module

val useCaseDomainModule = module {
    single<CreateWalletUseCase> { CreateWalletUseCase(get()) }
    single<GetNextWalletNumberUseCase> { GetNextWalletNumberUseCase(get()) }
    single<GetHasSessionUseCase> { GetHasSessionUseCase(get()) }
    single<GetAssetsByWalletUseCase> { GetAssetsByWalletUseCase(get()) }
    single<GetSessionUseCase> { GetSessionUseCase(get()) }
}
