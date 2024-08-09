package com.cdcoding.core.navigation

import com.cdcoding.core.navigation.core.Destination
import com.cdcoding.model.AssetId


sealed interface SendAssetDestination : Destination {
    data class SendAsset(val assetId: AssetId) : SendAssetDestination
}