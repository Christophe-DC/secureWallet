package com.cdcoding.selectasset.di

import cafe.adriel.voyager.core.registry.screenModule
import com.cdcoding.core.navigation.SelectAssetDestination
import com.cdcoding.selectasset.ui.SelectAssetScreen

val selectAssetScreenModule = screenModule {
    register<SelectAssetDestination.SelectAsset> { SelectAssetScreen() }
}
