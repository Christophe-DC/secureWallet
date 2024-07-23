package com.cdcoding.core.designsystem.state

import androidx.compose.runtime.State
import kotlinx.coroutines.flow.StateFlow

@Composable
actual fun <T> StateFlow<T>.collectAsStateWithLifecycle(): State<T> {
    TODO("Not yet implemented")
}