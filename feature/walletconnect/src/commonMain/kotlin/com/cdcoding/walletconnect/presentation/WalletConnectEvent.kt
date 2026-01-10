package com.cdcoding.walletconnect.presentation

sealed interface WalletConnectEvent {
    data class ShowToast(val message: String) : WalletConnectEvent
    data object NavigateBack : WalletConnectEvent
}
