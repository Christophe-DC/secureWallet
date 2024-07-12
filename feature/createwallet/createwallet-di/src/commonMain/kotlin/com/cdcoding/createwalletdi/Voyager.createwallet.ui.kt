package com.cdcoding.createwalletdi

import cafe.adriel.voyager.core.registry.screenModule
import com.cdcoding.core.navigation.CreateWalletDestination
import com.cdcoding.createwalletimpl.ui.CreateWalletScreen

val createWalletScreenModule = screenModule {
    register<CreateWalletDestination.CreateWallet> {
        CreateWalletScreen()
    }
}
