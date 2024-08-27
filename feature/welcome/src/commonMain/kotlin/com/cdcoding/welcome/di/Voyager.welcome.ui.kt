package com.cdcoding.welcome.di

import cafe.adriel.voyager.core.registry.screenModule
import com.cdcoding.core.navigation.WelcomeDestination
import com.cdcoding.welcome.ui.WelcomeScreen

val welcomeScreenModule = screenModule {
    register<WelcomeDestination.Welcome> {
        WelcomeScreen()
    }
}
