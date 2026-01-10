package com.cdcoding.walletconnect.di

import cafe.adriel.voyager.core.registry.screenModule
import com.cdcoding.core.navigation.WalletConnectDestination
import com.cdcoding.walletconnect.ui.WalletConnectScanScreen

val walletConnectScreenModule = screenModule {
    register<WalletConnectDestination.WalletConnectScan> { WalletConnectScanScreen() }
}