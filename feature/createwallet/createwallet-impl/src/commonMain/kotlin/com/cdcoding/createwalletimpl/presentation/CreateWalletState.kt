package com.cdcoding.createwalletimpl.presentation

import androidx.compose.runtime.Immutable

@Immutable
data class CreateWalletState(
    val walletName: String = "",
    val defaultWalletName: String = "",
)