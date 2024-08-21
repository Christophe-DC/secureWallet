package com.cdcoding.receiveasset.di

import cafe.adriel.voyager.core.registry.screenModule
import com.cdcoding.core.navigation.ReceiveAssetDestination
import com.cdcoding.receiveasset.ui.ReceiveAssetScreen

val receiveAssetScreenModule = screenModule {
    register<ReceiveAssetDestination.ReceiveAsset> { provider -> ReceiveAssetScreen(provider.assetId) }
}
