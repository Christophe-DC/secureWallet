package com.cdcoding.core.navigation.tab

import com.cdcoding.core.navigation.core.TabDestination


sealed interface WalletDetailDestination : TabDestination {
    data object WalletDetail : WalletDetailDestination
}
