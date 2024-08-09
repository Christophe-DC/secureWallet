package com.cdcoding.shared.di

import org.koin.dsl.module
import com.cdcoding.welcomeimpl.presentation.WelcomeViewModel
import com.cdcoding.createwalletimpl.presentation.CreateWalletViewModel
import com.cdcoding.homeimpl.presentation.HomeViewModel
import com.cdcoding.walletdetailimpl.presentation.WalletDetailViewModel
import com.cdcoding.selectasset.presentation.SelectAssetViewModel
import com.cdcoding.sendasset.presentation.SendAssetViewModel
import com.cdcoding.core.designsystem.components.AddressChainViewModel
import org.koin.core.module.dsl.singleOf

actual val viewModelModule = module {
    singleOf(::WelcomeViewModel)
    singleOf(::CreateWalletViewModel)
    singleOf(::HomeViewModel)
    singleOf(::WalletDetailViewModel)
    singleOf(::SelectAssetViewModel)
    singleOf(::SendAssetViewModel)
    singleOf(::AddressChainViewModel)
}
