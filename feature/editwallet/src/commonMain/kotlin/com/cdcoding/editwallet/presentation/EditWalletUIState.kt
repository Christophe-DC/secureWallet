package com.cdcoding.editwallet.presentation



data class EditWalletUIState(
    val walletId: String = "",
    val walletName: String = "",
    val walletAddress: String = "",
    val chainIconUrl: String = "",
    val error: String? = null,
)
