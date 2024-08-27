package com.cdcoding.core.designsystem.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.compose.collectAsStateWithLifecycle as androidCollectAsStateWithLifecycle

@Composable
actual fun <T> StateFlow<T>.collectAsStateWithLifecycle(): State<T> =
    androidCollectAsStateWithLifecycle()
