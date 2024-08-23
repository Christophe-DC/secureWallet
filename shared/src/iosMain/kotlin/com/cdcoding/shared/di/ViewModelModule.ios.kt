package com.cdcoding.shared.di

import org.koin.dsl.module
import com.cdcoding.welcome.presentation.WelcomeViewModel
import com.cdcoding.createwallet.presentation.CreateWalletViewModel
import com.cdcoding.importwallet.presentation.ImportWalletViewModel
import com.cdcoding.home.presentation.HomeViewModel
import com.cdcoding.walletdetail.presentation.WalletDetailViewModel
import com.cdcoding.selectasset.presentation.SelectAssetViewModel
import com.cdcoding.sendasset.presentation.SendAssetViewModel
import com.cdcoding.receiveasset.presentation.ReceiveAssetViewModel
import com.cdcoding.amount.presentation.AmountViewModel
import com.cdcoding.confirm.presentation.ConfirmViewModel
import com.cdcoding.core.designsystem.components.AddressChainViewModel
import com.cdcoding.selectwallet.presentation.SelectWalletViewModel
import org.koin.core.module.dsl.singleOf

actual val viewModelModule = module {
    singleOf(::WelcomeViewModel)
    singleOf(::CreateWalletViewModel)
    singleOf(::HomeViewModel)
    singleOf(::WalletDetailViewModel)
    singleOf(::SelectAssetViewModel)
    singleOf(::SendAssetViewModel)
    singleOf(::AddressChainViewModel)
    singleOf(::AmountViewModel)
    singleOf(::ConfirmViewModel)
    singleOf(::ReceiveAssetViewModel)
    singleOf(::ImportWalletViewModel)
    singleOf(::SelectWalletViewModel)
}
