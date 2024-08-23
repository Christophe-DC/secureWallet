package com.cdcoding.editwallet.presentation

sealed class EditWalletEvent {
    data class ShowToast(val message: String): EditWalletEvent()
}