package com.cdcoding.walletconnect.presentation

import com.cdcoding.model.WcProposal

enum class WalletConnectScreenMode {
    Idle,
    Scanning,
    Pairing,
    WaitingProposal,
    Confirming,
    Connected,
    Error,
}

data class WalletConnectUIState(
    val walletAccounts: List<com.cdcoding.model.Account> = emptyList(),
    val mode: WalletConnectScreenMode = WalletConnectScreenMode.Idle,

    val uriInput: String = "",
    val lastError: String? = null,

    val proposal: WcProposal? = null,

    val sessionTopic: String? = null,
    val networksToConnect: List<String> = emptyList()
)
