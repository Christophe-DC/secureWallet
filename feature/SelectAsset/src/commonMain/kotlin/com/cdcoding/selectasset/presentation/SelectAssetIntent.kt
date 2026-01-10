package com.cdcoding.selectasset.presentation


sealed interface SelectAssetIntent {
    data class OnQueryChanged(val value: String) : SelectAssetIntent
}