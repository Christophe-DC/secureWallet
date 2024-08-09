package com.cdcoding.core.navigation

import com.cdcoding.core.navigation.core.Destination


sealed interface SelectAssetDestination : Destination {
    data object SelectAsset : SelectAssetDestination
}