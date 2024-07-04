package com.cdcoding.system.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable


@Composable
actual fun SecureWalletTheme(
    darkTheme: Boolean,
    dynamicColor: Boolean,
    content: @Composable() () -> Unit
) {

    MaterialTheme(
        colorScheme = if (darkTheme) darkScheme else lightScheme,
        shapes = shapes,
        content = content
    )
}
