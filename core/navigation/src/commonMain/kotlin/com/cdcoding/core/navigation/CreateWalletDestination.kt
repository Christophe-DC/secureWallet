package com.cdcoding.core.navigation

import com.cdcoding.core.navigation.core.Destination


sealed interface CreateWalletDestination : Destination {
    data object CreateWallet : CreateWalletDestination
}
