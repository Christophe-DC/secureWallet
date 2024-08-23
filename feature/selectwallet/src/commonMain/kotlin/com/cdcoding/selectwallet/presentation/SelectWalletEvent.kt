package com.cdcoding.selectwallet.presentation

sealed class SelectWalletEvent {
    data class ShowToast(val message: String): SelectWalletEvent()
}