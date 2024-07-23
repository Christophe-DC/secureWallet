package com.cdcoding.core.designsystem.hooks

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import org.koin.compose.LocalKoinScope
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope


@Composable
inline fun useScope(): CoroutineScope {
    return rememberCoroutineScope()
}

@Composable
inline fun useSnackbar(): SnackbarHostState {
    return remember { SnackbarHostState() }
}

@Composable
inline fun useEffect(vararg keys: Any, noinline block: suspend CoroutineScope.() -> Unit) {
    LaunchedEffect(keys = keys, block = block)
}

@Composable
inline fun useEffect(key1: Any, noinline block: suspend CoroutineScope.() -> Unit) {
    LaunchedEffect(key1 = key1, block = block)
}

@Composable
inline fun <reified T> useInject(
    qualifier: Qualifier? = null,
    scope: Scope = LocalKoinScope.current,
    noinline parameters: ParametersDefinition? = null,
): T {
  return remember(qualifier, scope, parameters) { scope.get(qualifier, parameters) }
}
