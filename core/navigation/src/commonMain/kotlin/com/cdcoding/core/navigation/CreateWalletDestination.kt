package com.cdcoding.core.navigation

import com.cdcoding.core.navigation.core.Destination


sealed interface CreateWalletDestination : Destination {
    object CreateWallet : CreateWalletDestination
}
