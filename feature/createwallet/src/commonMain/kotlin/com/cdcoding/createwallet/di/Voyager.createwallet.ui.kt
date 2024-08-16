package com.cdcoding.createwallet.di

import cafe.adriel.voyager.core.registry.screenModule
import com.cdcoding.core.navigation.CreateWalletDestination
import com.cdcoding.createwallet.ui.CreateWalletScreen

val createWalletScreenModule = screenModule {
    register<CreateWalletDestination.CreateWallet> {
        CreateWalletScreen()
    }
}
