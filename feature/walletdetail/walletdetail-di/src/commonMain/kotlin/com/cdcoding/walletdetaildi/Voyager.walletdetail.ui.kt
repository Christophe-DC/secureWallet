package com.cdcoding.walletdetaildi

import com.cdcoding.core.navigation.tab.WalletDetailDestination
import com.cdcoding.core.navigation.tab.registry.tabModule
import com.cdcoding.walletdetailimpl.ui.WalletDetailScreen

val walletDetailScreenModule = tabModule {
    register<WalletDetailDestination.WalletDetail> {
        WalletDetailScreen()
    }
}
