package com.cdcoding.core.navigation

import com.cdcoding.core.navigation.core.Destination


sealed interface HomeDestination : Destination {
    data class Home(val homeDestinationEvent: HomeDestinationEvent) : HomeDestination
}

sealed interface HomeDestinationEvent {
    data object WalletCreated: HomeDestinationEvent
    data object None: HomeDestinationEvent
}