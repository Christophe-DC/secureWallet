package com.cdcoding.core.navigation

import com.cdcoding.core.navigation.core.Destination


sealed interface HomeDestination : Destination {
    data object Home : HomeDestination
}