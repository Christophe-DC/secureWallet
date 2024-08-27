package com.cdcoding.selectwallet.presentation

import com.cdcoding.model.IconUrl


data class SelectWalletUIState(
    val currentWalletId: String = "",
    val wallets: List<WalletItemUIState> = emptyList()
)

data class WalletItemUIState(
    val id: String,
    val name: String,
    val icon: IconUrl = ""
)