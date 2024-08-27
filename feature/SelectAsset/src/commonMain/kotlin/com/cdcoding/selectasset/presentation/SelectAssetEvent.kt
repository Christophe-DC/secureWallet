package com.cdcoding.selectasset.presentation

sealed class SelectAssetEvent {
    data class ShowToast(val message: String): SelectAssetEvent()
}