package com.cdcoding.core.navigation

import com.cdcoding.core.navigation.core.Destination


sealed interface WelcomeDestination : Destination {
    data object Welcome : WelcomeDestination
}
