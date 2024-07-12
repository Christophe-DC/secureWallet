package com.cdcoding.core.navigation

import com.cdcoding.core.navigation.core.Destination


sealed interface ImportWalletDestination : Destination {
    object ImportWallet : ImportWalletDestination
}
