package com.cdcoding.sendasset.di

import cafe.adriel.voyager.core.registry.screenModule
import com.cdcoding.core.navigation.SendAssetDestination
import com.cdcoding.sendasset.ui.SendAssetScreen

val sendAssetScreenModule = screenModule {
    register<SendAssetDestination.SendAsset> { provider -> SendAssetScreen(provider.assetId) }
}
