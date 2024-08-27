package com.cdcoding.editwallet.di

import cafe.adriel.voyager.core.registry.screenModule
import com.cdcoding.core.navigation.EditWalletDestination
import com.cdcoding.editwallet.ui.EditWalletScreen

val editWalletScreenModule = screenModule {
    register<EditWalletDestination.EditWallet> { provider -> EditWalletScreen(provider.walletId) }
}
