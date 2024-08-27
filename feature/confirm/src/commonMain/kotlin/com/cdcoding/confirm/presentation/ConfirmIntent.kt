package com.cdcoding.confirm.presentation

sealed class ConfirmIntent {
    data object OnSend : ConfirmIntent()
}