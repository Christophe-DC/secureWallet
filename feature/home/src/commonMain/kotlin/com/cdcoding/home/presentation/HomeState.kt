package com.cdcoding.home.presentation

import androidx.compose.runtime.Immutable

@Immutable
data class HomeState(
    val hasSession: Boolean = false,
)