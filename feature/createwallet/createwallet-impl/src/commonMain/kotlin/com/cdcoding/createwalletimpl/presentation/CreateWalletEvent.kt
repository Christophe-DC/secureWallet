package com.cdcoding.createwalletimpl.presentation

sealed interface CreateWalletEvent {
    object OnCreateNewWallet : CreateWalletEvent
    data class OnWalletNameChanged(val name: String) : CreateWalletEvent
}