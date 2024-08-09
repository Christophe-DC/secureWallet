package com.cdcoding.sendasset.presentation

import com.cdcoding.model.AssetInfo


data class SendAssetUIState(
    val isLoading: Boolean = false,
    val assetInfo: AssetInfo? = null,
    val address: String = "",
    val addressDomain: String = "",
    val memo: String = "",
    val hasMemo: Boolean = false,
    val addressError: RecipientFormError = RecipientFormError.None,
    val memoError: RecipientFormError = RecipientFormError.None,
    val screen: SendAssetScreen = SendAssetScreen.Idle
)


sealed interface RecipientFormError {
    data object None : RecipientFormError
    data object IncorrectAddress : RecipientFormError
}

sealed interface SendAssetScreen {
    data object Loading : SendAssetScreen
    data object Idle : SendAssetScreen
    data class ScanQr(val scanType: ScanType) : SendAssetScreen
    data class Fatal(val error: String) : SendAssetScreen
}

enum class ScanType {
    Address,
    Memo,
}