package com.cdcoding.securewallet.di

import org.koin.dsl.module
import com.cdcoding.welcomeimpl.presentation.WelcomeViewModel
import com.cdcoding.createwalletimpl.presentation.CreateWalletViewModel
import com.cdcoding.homeimpl.presentation.HomeViewModel
import org.koin.core.module.dsl.singleOf


actual val viewModelModule = module {
    singleOf(::WelcomeViewModel)
    singleOf(::CreateWalletViewModel)
    singleOf(::HomeViewModel)
}
