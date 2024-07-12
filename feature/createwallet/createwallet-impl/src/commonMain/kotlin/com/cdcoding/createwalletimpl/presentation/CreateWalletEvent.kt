package com.cdcoding.createwalletimpl.presentation

sealed interface CreateWalletEvent {
    object OnCreateNewWallet : CreateWalletEvent
}