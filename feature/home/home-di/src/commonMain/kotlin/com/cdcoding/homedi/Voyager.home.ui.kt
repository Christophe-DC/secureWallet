package com.cdcoding.homedi

import cafe.adriel.voyager.core.registry.screenModule
import com.cdcoding.core.navigation.HomeDestination
import com.cdcoding.homeimpl.ui.HomeScreen

val homeScreenModule = screenModule {
    register<HomeDestination.Home> {provider ->
        HomeScreen(provider.homeDestinationEvent)
    }
}
