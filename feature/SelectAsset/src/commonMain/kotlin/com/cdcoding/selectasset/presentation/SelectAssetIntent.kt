package com.cdcoding.selectasset.presentation

import com.cdcoding.model.AssetId

sealed class SelectAssetIntent {
    data class OnSelect(val assetId: AssetId): SelectAssetIntent()
}