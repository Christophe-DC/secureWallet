package com.cdcoding.amount.presentation

import com.cdcoding.model.ConfirmParams

sealed class AmountIntent {
    data class OnUpdateAmount(val input: String, val maxAmount: Boolean = false) : AmountIntent()
    data class OnNext(val onNextComplete: (confirmParams: ConfirmParams) -> Unit) : AmountIntent()
    data object OnMaxAmount : AmountIntent()
}