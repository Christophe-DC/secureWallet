package com.cdcoding.shared.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import com.cdcoding.welcomeimpl.presentation.WelcomeViewModel
import com.cdcoding.createwalletimpl.presentation.CreateWalletViewModel
import com.cdcoding.homeimpl.presentation.HomeViewModel
import com.cdcoding.walletdetailimpl.presentation.WalletDetailViewModel
import com.cdcoding.selectasset.presentation.SelectAssetViewModel
import com.cdcoding.sendasset.presentation.SendAssetViewModel
import com.cdcoding.amount.presentation.AmountViewModel
import com.cdcoding.confirm.presentation.ConfirmViewModel
import com.cdcoding.core.designsystem.components.AddressChainViewModel

actual val viewModelModule = module {
    viewModelOf(::WelcomeViewModel)
    viewModelOf(::CreateWalletViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::WalletDetailViewModel)
    viewModelOf(::SelectAssetViewModel)
    viewModelOf(::SendAssetViewModel)
    viewModelOf(::AddressChainViewModel)
    viewModelOf(::AmountViewModel)
    viewModelOf(::ConfirmViewModel)
}
