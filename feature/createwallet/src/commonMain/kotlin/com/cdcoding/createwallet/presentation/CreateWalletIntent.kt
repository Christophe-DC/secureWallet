package com.cdcoding.createwallet.presentation

sealed interface CreateWalletIntent {
    object OnCreateNewWallet : CreateWalletIntent
    data class OnWalletNameChanged(val name: String) : CreateWalletIntent
}