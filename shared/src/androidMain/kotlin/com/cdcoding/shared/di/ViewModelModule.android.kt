package com.cdcoding.shared.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import com.cdcoding.welcomeimpl.presentation.WelcomeViewModel
import com.cdcoding.createwalletimpl.presentation.CreateWalletViewModel
import com.cdcoding.homeimpl.presentation.HomeViewModel
import com.cdcoding.walletdetailimpl.presentation.WalletDetailViewModel

actual val viewModelModule = module {
    viewModelOf(::WelcomeViewModel)
    viewModelOf(::CreateWalletViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::WalletDetailViewModel)
}
