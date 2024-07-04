package com.cdcoding.welcomedi

import cafe.adriel.voyager.core.registry.screenModule
import com.cdcoding.core.navigation.WelcomeDestination
import com.cdcoding.welcomeimpl.ui.WelcomeScreen

val welcomeScreenModule = screenModule {
    register<WelcomeDestination.Welcome> {
        WelcomeScreen()
    }
}
