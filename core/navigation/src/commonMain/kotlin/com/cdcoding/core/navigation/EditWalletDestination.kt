package com.cdcoding.core.navigation

import com.cdcoding.core.navigation.core.Destination


sealed interface EditWalletDestination : Destination {
    data class EditWallet(val walletId: String) : EditWalletDestination
}