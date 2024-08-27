package com.cdcoding.receiveasset.presentation

sealed class ReceiveAssetEvent {
    data class ShowToast(val message: String): ReceiveAssetEvent()
}