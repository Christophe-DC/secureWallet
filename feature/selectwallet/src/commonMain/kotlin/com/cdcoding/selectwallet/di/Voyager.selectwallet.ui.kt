package com.cdcoding.selectwallet.di

import cafe.adriel.voyager.core.registry.screenModule
import com.cdcoding.core.navigation.SelectWalletDestination
import com.cdcoding.selectwallet.ui.SelectWalletScreen

val selectWalletScreenModule = screenModule {
    register<SelectWalletDestination.SelectWallet> { SelectWalletScreen() }
}
