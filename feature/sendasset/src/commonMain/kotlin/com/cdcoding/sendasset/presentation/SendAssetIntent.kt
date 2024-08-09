package com.cdcoding.sendasset.presentation

import com.cdcoding.model.NameRecord

sealed class SendAssetIntent {
    data class OnValueChange(val input: String, val nameRecord: NameRecord?) : SendAssetIntent()
    data class SetQrData(val data: String) : SendAssetIntent()
    data class OnQrScanner(val scanType: ScanType) : SendAssetIntent()
    data object OnScanCanceled : SendAssetIntent()
    data class OnNext(val input: String,
                      val nameRecord: NameRecord?,
                      val memo: String,
                      val onRecipientComplete: () -> Unit) : SendAssetIntent()
}