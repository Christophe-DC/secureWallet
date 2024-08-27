package com.cdcoding.home.presentation

import androidx.compose.runtime.Immutable

@Immutable
data class HomeUIState(
    val hasSession: Boolean = true,
)