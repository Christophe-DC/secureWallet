package com.cdcoding.walletconnect.presentation

import androidx.lifecycle.viewModelScope
import com.cdcoding.common.utils.CommonViewModel
import com.cdcoding.data.repository.SessionRepository
import com.cdcoding.data.repository.WalletConnectRepository
import com.cdcoding.domain.walletconnect.ApproveWalletConnectProposalUseCase
import com.cdcoding.domain.walletconnect.DisconnectAllWalletConnectUseCase
import com.cdcoding.domain.walletconnect.DisconnectWalletConnectUseCase
import com.cdcoding.domain.walletconnect.ObserveWalletConnectEventsUseCase
import com.cdcoding.domain.walletconnect.PairWalletConnectUseCase
import com.cdcoding.domain.walletconnect.PreviewWalletConnectApprovedChainsUseCase
import com.cdcoding.domain.walletconnect.RejectWalletConnectProposalUseCase
import com.cdcoding.domain.walletconnect.RespondWalletConnectErrorUseCase
import com.cdcoding.domain.walletconnect.RespondWalletConnectResultUseCase
import com.cdcoding.domain.walletconnect.SignPersonalMessageUseCase
import com.cdcoding.model.Chain
import com.cdcoding.model.WcEvent
import com.cdcoding.model.WcSessionRequest
import kotlinx.coroutines.launch

class WalletConnectViewModel(
    private val sessionRepository: SessionRepository,
    private val observeEvents: ObserveWalletConnectEventsUseCase,
    private val pairUseCase: PairWalletConnectUseCase,
    private val approveUseCase: ApproveWalletConnectProposalUseCase,
    private val rejectUseCase: RejectWalletConnectProposalUseCase,
    private val respondResultUseCase: RespondWalletConnectResultUseCase,
    private val respondErrorUseCase: RespondWalletConnectErrorUseCase,
    private val disconnectUseCase: DisconnectWalletConnectUseCase,
    private val disconnectAllUseCase: DisconnectAllWalletConnectUseCase,
    private val signPersonalMessageUseCase: SignPersonalMessageUseCase,
    private val previewWalletConnectApprovedChainsUseCase: PreviewWalletConnectApprovedChainsUseCase
) : CommonViewModel<WalletConnectUIState, WalletConnectEvent, WalletConnectIntent>() {

    override fun createInitialState(): WalletConnectUIState = WalletConnectUIState()

    init {
        viewModelScope.launch {
            observeEvents().collect { event ->
                when (event) {
                    is WcEvent.ProposalReceived -> {

                        val networksToConnect = runCatching {
                            previewWalletConnectApprovedChainsUseCase(
                                proposerPublicKey = event.proposal.proposerPublicKey,
                                requestedNamespaces = event.proposal.requestedNamespaces
                            )
                        }.getOrElse { emptyList() }

                        setState {
                            copy(
                                walletAccounts = sessionRepository.getSession()?.wallet?.accounts.orEmpty(),
                                mode = WalletConnectScreenMode.Confirming,
                                proposal = event.proposal,
                                lastError = null,
                                networksToConnect = networksToConnect
                            )
                        }
                    }

                    is WcEvent.SessionApproved -> {
                        setState {
                            copy(
                                mode = WalletConnectScreenMode.Connected,
                                sessionTopic = event.topic,
                                lastError = null
                            )
                        }

                        setEvent { WalletConnectEvent.ShowToast("WalletConnect connected") }
                    }

                    is WcEvent.SessionRejected -> {
                        setState {
                            copy(
                                mode = WalletConnectScreenMode.Idle,
                                proposal = null,
                            )
                        }
                        setEvent { WalletConnectEvent.ShowToast("Connection rejected") }
                    }

                    is WcEvent.RequestReceived -> {
                        if (event.request.method == "personal_sign") {
                            handlePersonalSign(event.request)
                        } else {
                            viewModelScope.launch {
                                respondErrorUseCase(
                                    topic = event.request.topic,
                                    requestId = event.request.id,
                                    code = 4200,
                                    message = "Unsupported method ${event.request.method}"
                                )
                            }
                        }
                    }

                    is WcEvent.SessionExtended -> {
                        setEvent { WalletConnectEvent.ShowToast("Session extended") }
                    }

                    is WcEvent.Disconnected -> {
                        setState {
                            copy(
                                mode = WalletConnectScreenMode.Idle,
                                sessionTopic = null,
                                proposal = null,
                            )
                        }
                        setEvent { WalletConnectEvent.ShowToast("Session disconnected") }
                    }

                    is WcEvent.Error -> {
                        setState { copy(mode = WalletConnectScreenMode.Error, lastError = event.message) }
                        setEvent { WalletConnectEvent.ShowToast(event.message) }
                    }
                }
            }
        }
    }

    override fun handleIntent(intent: WalletConnectIntent) {
        when (intent) {
            WalletConnectIntent.OnScreenShown -> Unit

            is WalletConnectIntent.OnUriChanged -> {
                setState { copy(uriInput = intent.value, lastError = null) }
            }

            WalletConnectIntent.OnPasteAndPair -> {
                viewModelScope.launch { pair(uiState.value.uriInput) }
            }

            WalletConnectIntent.OnOpenScanner -> {
                setState { copy(mode = WalletConnectScreenMode.Scanning) }
            }

            is WalletConnectIntent.OnQrScanned -> {
                setState { copy(uriInput = intent.uri, mode = WalletConnectScreenMode.Pairing) }
                viewModelScope.launch { pair(intent.uri) }
            }

            WalletConnectIntent.OnScanCanceled -> {
                setState { copy(mode = WalletConnectScreenMode.Idle) }
            }

            WalletConnectIntent.OnConfirmReject -> {
                val proposalPk = uiState.value.proposal?.proposerPublicKey ?: return
                viewModelScope.launch {
                    rejectUseCase(proposalPk, "User rejected")
                    setState {
                        copy(
                            proposal = null,
                            mode = WalletConnectScreenMode.Idle
                        )
                    }
                }
            }

            WalletConnectIntent.OnConfirmApprove -> {
                val proposal = uiState.value.proposal ?: return
                viewModelScope.launch {
                    approveUseCase(
                        proposerPublicKey = proposal.proposerPublicKey
                    )

                }
            }

            WalletConnectIntent.OnDisconnect -> {
                val topic = uiState.value.sessionTopic ?: return
                viewModelScope.launch { disconnectUseCase(topic) }
            }

            /*WalletConnectIntent.OnDisconnectAll -> {
                viewModelScope.launch { disconnectAllUseCase() }
            }*/
        }
    }

    private suspend fun pair(uri: String) {
        if (uri.isBlank()) {
            setEvent { WalletConnectEvent.ShowToast("WalletConnect URI empty") }
            setState { copy(mode = WalletConnectScreenMode.Idle) }
            return
        }

        setState { copy(mode = WalletConnectScreenMode.Pairing, lastError = null) }

        runCatching {
            pairUseCase(uri)
            setState { copy(mode = WalletConnectScreenMode.WaitingProposal) }
        }.onFailure {
            setState { copy(mode = WalletConnectScreenMode.Error, lastError = it.message ?: "Pair failed") }
            setEvent { WalletConnectEvent.ShowToast(it.message ?: "Pair failed") }
        }
    }


    private fun handlePersonalSign(request: WcSessionRequest) {
        viewModelScope.launch {
            val messageParam = request.params.firstOrNull().orEmpty()
            val sig = signPersonalMessageUseCase(messageParam)

            sig.fold(
                onSuccess = { bytes ->
                    val hex = "0x" + bytes.joinToString("") { "%02x".format(it) }
                    respondResultUseCase(topic = request.topic, requestId = request.id, result = hex)
                },
                onFailure = { err ->
                    respondErrorUseCase(topic = request.topic, requestId = request.id, code = 4001, message = err.message ?: "User rejected")
                }
            )
        }
    }
}
