package com.cdcoding.editwallet.presentation

import com.cdcoding.model.Wallet

sealed class EditWalletIntent {
    data class SetWalletName(val walletName: String) : EditWalletIntent()
    data class OnDeleteWallet(val onBoard: () -> Unit) : EditWalletIntent()
}