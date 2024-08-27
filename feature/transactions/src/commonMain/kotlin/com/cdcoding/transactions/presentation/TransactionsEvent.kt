package com.cdcoding.transactions.presentation

sealed class TransactionsEvent {
    data class ShowToast(val message: String): TransactionsEvent()
}