package com.cdcoding.sendasset.presentation

sealed class SendAssetEvent {
    data class ShowToast(val message: String): SendAssetEvent()
}