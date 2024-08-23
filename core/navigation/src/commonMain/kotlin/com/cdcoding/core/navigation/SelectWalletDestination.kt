package com.cdcoding.core.navigation

import com.cdcoding.core.navigation.core.Destination


sealed interface SelectWalletDestination : Destination {
    data object SelectWallet : SelectWalletDestination
}