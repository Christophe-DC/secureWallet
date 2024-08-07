package com.cdcoding.core.designsystem.util

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.cdcoding.model.PriceState

@Composable
fun priceColor(state: PriceState) = when (state) {
    PriceState.Up -> MaterialTheme.colorScheme.tertiary
    PriceState.Down -> MaterialTheme.colorScheme.error
    PriceState.None -> MaterialTheme.colorScheme.secondary
}