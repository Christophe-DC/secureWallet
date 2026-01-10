package com.cdcoding.shared.di

import com.cdcoding.amount.presentation.AmountViewModel
import com.cdcoding.confirm.presentation.ConfirmViewModel
import com.cdcoding.core.designsystem.components.AddressChainViewModel
import com.cdcoding.createwallet.presentation.CreateWalletViewModel
import com.cdcoding.editwallet.presentation.EditWalletViewModel
import com.cdcoding.home.presentation.HomeViewModel
import com.cdcoding.importwallet.presentation.ImportWalletViewModel
import com.cdcoding.receiveasset.presentation.ReceiveAssetViewModel
import com.cdcoding.selectasset.presentation.SelectAssetViewModel
import com.cdcoding.selectwallet.presentation.SelectWalletViewModel
import com.cdcoding.sendasset.presentation.SendAssetViewModel
import com.cdcoding.showphrase.presentation.ShowPhraseViewModel
import com.cdcoding.transactions.presentation.TransactionsViewModel
import com.cdcoding.walletconnect.presentation.WalletConnectViewModel
import com.cdcoding.walletdetail.presentation.WalletDetailViewModel
import com.cdcoding.welcome.presentation.WelcomeViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

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
    singleOf(::EditWalletViewModel)
    singleOf(::ShowPhraseViewModel)
    singleOf(::TransactionsViewModel)
    singleOf(::WalletConnectViewModel)
}
