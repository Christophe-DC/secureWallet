package com.cdcoding.createwallet.presentation

import androidx.compose.runtime.Immutable

@Immutable
data class CreateWalletState(
    val walletIsCreating: Boolean = false,
    val walletName: String = "",
    val defaultWalletName: String = "",
)