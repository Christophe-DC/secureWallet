package com.cdcoding.selectwallet.presentation

import com.cdcoding.model.Wallet

sealed class SelectWalletIntent {
    data class OnWalletSelected(val walletId: String) : SelectWalletIntent()
    data class OnDeleteWallet(val walletId: String, val onBoard: () -> Unit) : SelectWalletIntent()
}