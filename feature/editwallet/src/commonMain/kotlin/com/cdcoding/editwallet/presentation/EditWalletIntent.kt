package com.cdcoding.editwallet.presentation

sealed class EditWalletIntent {
    data class SetWalletName(val walletName: String) : EditWalletIntent()
    data class OnDeleteWallet(val onBoard: () -> Unit) : EditWalletIntent()
}