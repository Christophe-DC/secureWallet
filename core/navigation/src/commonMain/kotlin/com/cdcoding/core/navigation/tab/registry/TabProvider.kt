package com.cdcoding.core.navigation.tab.registry

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.navigator.tab.Tab


@Composable
public inline fun <reified T : TabProvider> rememberTab(provider: T): Tab =
    remember(provider) {
        TabRegistry.get(provider)
    }

public interface TabProvider