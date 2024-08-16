package com.cdcoding.confirm.presentation

sealed class ConfirmEvent {
    data class ShowToast(val message: String): ConfirmEvent()
}