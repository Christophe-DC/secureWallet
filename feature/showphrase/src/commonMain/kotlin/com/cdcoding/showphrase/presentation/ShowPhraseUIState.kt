package com.cdcoding.showphrase.presentation

data class ShowPhraseUIState(
    val walletId: String = "",
    val walletName: String = "",
    val words: List<String> = emptyList(),
)
