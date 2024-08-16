package com.cdcoding.walletdetail.di

import com.cdcoding.core.navigation.tab.WalletDetailDestination
import com.cdcoding.core.navigation.tab.registry.tabModule
import com.cdcoding.walletdetail.ui.WalletDetailScreen

val walletDetailScreenModule = tabModule {
    register<WalletDetailDestination.WalletDetail> {
        WalletDetailScreen()
    }
}
