package com.cdcoding.securewallet.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import com.cdcoding.welcomeimpl.presentation.WelcomeViewModel
import com.cdcoding.createwalletimpl.presentation.CreateWalletViewModel

actual val viewModelModule = module {
    viewModelOf(::WelcomeViewModel)
    viewModelOf(::CreateWalletViewModel)
}
