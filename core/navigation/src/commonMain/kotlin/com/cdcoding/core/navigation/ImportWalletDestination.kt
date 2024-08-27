package com.cdcoding.core.navigation

import com.cdcoding.core.navigation.core.Destination


sealed interface ImportWalletDestination : Destination {
    data object ImportWallet : ImportWalletDestination
}
