package com.cdcoding.amount.presentation

sealed class AmountEvent {
    data class ShowToast(val message: String): AmountEvent()
}