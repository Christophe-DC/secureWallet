package com.cdcoding.core.navigation

import com.cdcoding.core.navigation.core.Destination
import com.cdcoding.model.SelectAssetType


sealed interface SelectAssetDestination : Destination {
    data class SelectAsset(val selectAssetType: SelectAssetType) : SelectAssetDestination
}