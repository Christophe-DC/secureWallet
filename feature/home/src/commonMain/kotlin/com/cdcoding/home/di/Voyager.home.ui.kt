package com.cdcoding.home.di

import cafe.adriel.voyager.core.registry.screenModule
import com.cdcoding.core.navigation.HomeDestination
import com.cdcoding.home.ui.HomeScreen

val homeScreenModule = screenModule {
    register<HomeDestination.Home> { HomeScreen() }
}
