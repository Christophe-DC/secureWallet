package com.cdcoding.transactions.presentation

sealed class TransactionsIntent {
    data object OnRefresh : TransactionsIntent()
}