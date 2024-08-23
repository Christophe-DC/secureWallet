package com.cdcoding.shared.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
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
import com.cdcoding.selectwallet.presentation.SelectWalletViewModel
import com.cdcoding.editwallet.presentation.EditWalletViewModel
import com.cdcoding.showphrase.presentation.ShowPhraseViewModel
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
    viewModelOf(::ReceiveAssetViewModel)
    viewModelOf(::ImportWalletViewModel)
    viewModelOf(::SelectWalletViewModel)
    viewModelOf(::EditWalletViewModel)
    viewModelOf(::ShowPhraseViewModel)
}
