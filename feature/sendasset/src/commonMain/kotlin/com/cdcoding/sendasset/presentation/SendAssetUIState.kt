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
    val screen: SendAssetStateScreen = SendAssetStateScreen.Idle
)


sealed interface RecipientFormError {
    data object None : RecipientFormError
    data object IncorrectAddress : RecipientFormError
}

sealed interface SendAssetStateScreen {
    data object Loading : SendAssetStateScreen
    data object Idle : SendAssetStateScreen
    data class ScanQr(val scanType: ScanType) : SendAssetStateScreen
    data class Fatal(val error: String) : SendAssetStateScreen
}

enum class ScanType {
    Address,
    Memo,
}