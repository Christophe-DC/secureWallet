package com.cdcoding.homeimpl.presentation

import androidx.compose.runtime.Immutable
import com.cdcoding.model.Wallet

@Immutable
data class HomeState(
    val wallet: Wallet? = null,
)