package com.cdcoding.importwallet.presentation

sealed class ImportWalletEvent {
    data class ShowToast(val message: String): ImportWalletEvent()
}