package com.cdcoding.core.navigation

import com.cdcoding.core.navigation.core.Destination

sealed interface WalletConnectDestination : Destination {
    data object WalletConnect : WalletConnectDestination
    data object WalletConnectScan : WalletConnectDestination
}