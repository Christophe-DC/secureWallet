package com.cdcoding.walletconnect.presentation

sealed interface WalletConnectIntent {
    data object OnScreenShown : WalletConnectIntent

    data class OnUriChanged(val value: String) : WalletConnectIntent
    data object OnPasteAndPair : WalletConnectIntent

    data object OnOpenScanner : WalletConnectIntent
    data class OnQrScanned(val uri: String) : WalletConnectIntent
    data object OnScanCanceled : WalletConnectIntent

    data object OnConfirmApprove : WalletConnectIntent
    data object OnConfirmReject : WalletConnectIntent

    data object OnDisconnect : WalletConnectIntent
}
