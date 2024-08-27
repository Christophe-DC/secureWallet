package com.cdcoding.transactions.presentation

import com.cdcoding.model.TransactionExtended

data class TransactionsUIState(
    val loading: Boolean = false,
    val transactions: List<TransactionExtended> = emptyList()
)
