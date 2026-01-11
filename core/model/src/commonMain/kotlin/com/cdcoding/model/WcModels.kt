package com.cdcoding.model

data class WcDappMeta(
    val name: String,
    val url: String? = null,
    val iconUrl: String? = null
)

data class WcProposal(
    val proposerPublicKey: String,
    val dapp: WcDappMeta,
    val requestedNamespaces: Map<String, WcNamespaceRequest>,
)

data class WcNamespaceRequest(
    val chains: List<String>,
    val methods: List<String>,
    val events: List<String>,
)

data class WcSessionRequest(
    val id: Long,
    val topic: String,
    val method: String,    // "personal_sign"
    val params: List<String> // pour EVM: souvent [message, address]
)

sealed interface WcEvent {
    data class ProposalReceived(val proposal: WcProposal) : WcEvent
    data class SessionApproved(val topic: String) : WcEvent
    data class SessionRejected(val proposerPublicKey: String) : WcEvent
    data class SessionExtended(val topic: String) : WcEvent
    data class RequestReceived(val request: WcSessionRequest) : WcEvent
    data class Disconnected(val topic: String?) : WcEvent
    data class Error(val message: String, val cause: Throwable? = null) : WcEvent
}