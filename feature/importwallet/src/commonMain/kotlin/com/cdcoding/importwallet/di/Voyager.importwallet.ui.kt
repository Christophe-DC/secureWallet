package com.cdcoding.importwallet.di

import cafe.adriel.voyager.core.registry.screenModule
import com.cdcoding.core.navigation.ImportWalletDestination
import com.cdcoding.importwallet.ui.ImportWalletScreen

val importWalletScreenModule = screenModule {
    register<ImportWalletDestination.ImportWallet> { ImportWalletScreen() }
}
