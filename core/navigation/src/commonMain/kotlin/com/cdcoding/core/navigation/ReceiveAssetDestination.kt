package com.cdcoding.core.navigation

import com.cdcoding.core.navigation.core.Destination
import com.cdcoding.model.AssetId


sealed interface ReceiveAssetDestination : Destination {
    data class ReceiveAsset(val assetId: AssetId) : ReceiveAssetDestination
}