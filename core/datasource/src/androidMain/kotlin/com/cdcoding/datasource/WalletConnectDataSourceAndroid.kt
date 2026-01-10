package com.cdcoding.walletconnect.datasource

import android.app.Application
import android.util.Log
import com.cdcoding.common.utils.caip
import com.cdcoding.datasource.WalletConnectDataSource
import com.cdcoding.model.Account
import com.cdcoding.model.WcDappMeta
import com.cdcoding.model.WcEvent
import com.cdcoding.model.WcNamespaceRequest
import com.cdcoding.model.WcProposal
import com.cdcoding.model.WcSessionRequest
import com.reown.android.Core
import com.reown.android.CoreClient
import com.reown.android.relay.ConnectionType
import com.reown.walletkit.client.Wallet
import com.reown.walletkit.client.WalletKit
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class WalletConnectDataSourceAndroid(
    private val application: Application,
    private val projectId: String,
    private val connectionType: ConnectionType = ConnectionType.AUTOMATIC,
    private val telemetryEnabled: Boolean = true,
) : WalletConnectDataSource {

    private val _events = MutableSharedFlow<WcEvent>(extraBufferCapacity = 64)
    override val events = _events.asSharedFlow()

    private val proposalsByPk = mutableMapOf<String, Wallet.Model.SessionProposal>()

    init {
        initSdk()
        setDelegate()
    }

    private fun initSdk() {
        runCatching {
            val appMetaData = Core.Model.AppMetaData(
                name = "SecureWallet",
                description = "SecureWallet WalletConnect",
                url = "https://securewallet.app",
                icons = listOf("https://securewallet.app/icon.png"),
                redirect = "securewallet://wc"
            )

            CoreClient.initialize(
                application = application,
                projectId = projectId,
                metaData = appMetaData,
                connectionType = connectionType,
                relay = null,
                keyServerUrl = null,
                networkClientTimeout = null,
                telemetryEnabled = telemetryEnabled,
                onError = { error ->
                    _events.tryEmit(
                        WcEvent.Error(
                            error.throwable.message ?: "CoreClient init error",
                            error.throwable
                        )
                    )
                }
            )

            val initParams = Wallet.Params.Init(core = CoreClient)

            WalletKit.initialize(initParams) { error ->
                if (error != null) {
                    _events.tryEmit(WcEvent.Error(error.throwable.message ?: "WalletKit init error", error.throwable))
                }
            }
        }.onFailure {
            _events.tryEmit(WcEvent.Error(it.message ?: "WalletConnect init failed", it))
        }
    }

    private fun setDelegate() {
        WalletKit.setWalletDelegate(object : WalletKit.WalletDelegate {

            override fun onSessionProposal(
                sessionProposal: Wallet.Model.SessionProposal,
                verifyContext: Wallet.Model.VerifyContext
            ) {

                val pk = sessionProposal.proposerPublicKey
                proposalsByPk[pk] = sessionProposal

                val dapp = WcDappMeta(
                    name = sessionProposal.name,
                    url = sessionProposal.url,
                    iconUrl = sessionProposal.icons.firstOrNull()?.path
                )

                val required = sessionProposal.requiredNamespaces.mapValues { (_, ns) ->
                    WcNamespaceRequest(
                        chains = ns.chains.orEmpty(),
                        methods = ns.methods.orEmpty(),
                        events = ns.events.orEmpty()
                    )
                }

                _events.tryEmit(
                    WcEvent.ProposalReceived(
                        WcProposal(
                            proposerPublicKey = pk,
                            dapp = dapp,
                            requiredNamespaces = required
                        )
                    )
                )


                /*val pk = sessionProposal.proposerPublicKey
                proposalsByPk[pk] = sessionProposal

                val dapp = WcDappMeta(
                    name = sessionProposal.name,
                    url = sessionProposal.url,
                    iconUrl = sessionProposal.icons.firstOrNull()?.path
                )

                val requiredEip155 = sessionProposal.requiredNamespaces["eip155"]
                val requiredChains = requiredEip155?.chains.orEmpty()
                val requiredMethods = requiredEip155?.methods.orEmpty()

                _events.tryEmit(
                    WcEvent.ProposalReceived(
                        WcProposal(
                            proposerPublicKey = sessionProposal.proposerPublicKey,
                            dapp = dapp,
                            requiredChains = requiredChains,
                            requiredMethods = requiredMethods
                        )
                    )
                )*/
            }

            override fun onSessionRequest(
                sessionRequest: Wallet.Model.SessionRequest,
                verifyContext: Wallet.Model.VerifyContext
            ) {
                val topic = sessionRequest.topic
                val id = sessionRequest.request.id
                val method = sessionRequest.request.method

                /*val paramsList: List<String> = when (val p = sessionRequest.request.params) {
                    is List<*> -> p.mapNotNull { it?.toString() }
                    null -> emptyList()
                    else -> listOf(p.toString())
                }*/
                val paramsList =  listOf(sessionRequest.request.params)

                _events.tryEmit(
                    WcEvent.RequestReceived(
                        WcSessionRequest(
                            id = id,
                            topic = topic,
                            method = method,
                            params = paramsList
                        )
                    )
                )
            }

            override fun onSessionDelete(sessionDelete: Wallet.Model.SessionDelete) {
                when (sessionDelete) {
                    is Wallet.Model.SessionDelete.Success -> {
                        _events.tryEmit(WcEvent.Disconnected(sessionDelete.topic))
                    }
                    is Wallet.Model.SessionDelete.Error -> {
                        _events.tryEmit(
                            WcEvent.Error(
                                sessionDelete.error.message ?: "SessionDelete error",
                                sessionDelete.error
                            )
                        )
                    }
                }
            }

            override fun onSessionExtend(session: Wallet.Model.Session) {
                val topic = session.topic
                _events.tryEmit(WcEvent.SessionExtended(topic))
            }

            override fun onSessionSettleResponse(settleSessionResponse: Wallet.Model.SettledSessionResponse) {
                when (settleSessionResponse) {
                    is Wallet.Model.SettledSessionResponse.Result -> {
                        _events.tryEmit(WcEvent.SessionApproved(settleSessionResponse.session.topic))
                    }
                    is Wallet.Model.SettledSessionResponse.Error -> {
                        _events.tryEmit(
                            WcEvent.Error(
                                settleSessionResponse.errorMessage
                            )
                        )
                    }
                }
            }

            override fun onConnectionStateChange(state: Wallet.Model.ConnectionState) {
                Log.d("WC", "ConnectionState: $state")
            }

            override fun onError(error: Wallet.Model.Error) {
                _events.tryEmit(WcEvent.Error(error.throwable.message ?: "WalletConnect error", error.throwable))
            }

            // Not used in your scope (for now)
            override fun onSessionUpdateResponse(sessionUpdateResponse: Wallet.Model.SessionUpdateResponse) = Unit

        })
    }

    override suspend fun pair(uri: String) {
        runCatching {
            WalletKit.pair(Wallet.Params.Pair(uri)) { error ->
                if (error != null) {
                    _events.tryEmit(WcEvent.Error(error.throwable.message ?: "Pair error", error.throwable))
                }
            }
        }.onFailure {
            _events.tryEmit(WcEvent.Error(it.message ?: "Pair failed", it))
        }
    }

    /**
     * Variante 2:
     * - Construit supportedNamespaces (ce que TON wallet supporte),
     * - Utilise WalletKit.generateApprovedNamespaces(proposal, supportedNamespaces)
     * - Approve via proposerPublicKey
     */
    override suspend fun approve(
        proposerPublicKey: String,
        accounts: List<Account>
    ) {
        val proposal = proposalsByPk[proposerPublicKey]
        if (proposal == null) {
            _events.tryEmit(WcEvent.Error("Missing SessionProposal for proposerPublicKey=$proposerPublicKey"))
            return
        }

        runCatching {
            if (accounts.isEmpty()) {
                _events.tryEmit(WcEvent.Error("No accounts available for session approval"))
                return
            }



            val supportedNamespaces =
                buildSupportedNamespacesFromWallet(accounts)


            /*// 1) Ce que TON wallet supporte (EVM only)
            //    - chains: tu passes celles que tu supportes réellement (ex: eip155:1 + eip155:137)
            //    - methods: tu passes celles que tu supportes réellement (tu peux mettre plus tard eth_signTypedData, etc.)
            //    - events: selon doc EVM: chainChanged/accountsChanged
            val supportedNamespaces = mapOf(
                "eip155" to Wallet.Model.Namespace.Session(
                    chains = chains,                 // tes chains supportées
                    methods = methods,               // tes méthodes supportées
                    events = listOf("chainChanged", "accountsChanged"),
                    accounts = accounts              // CAIP10 accounts
                )
            )*/

            // 2) Helper WalletKit pour sortir les namespaces approuvables
            val approvedNamespaces =
                WalletKit.generateApprovedNamespaces(proposal, supportedNamespaces)

            val approveParams =
                Wallet.Params.SessionApprove(proposal.proposerPublicKey, approvedNamespaces)

            WalletKit.approveSession(approveParams) { error ->
                if (error != null) {
                    _events.tryEmit(WcEvent.Error(error.throwable.message ?: "Approve error", error.throwable))
                }
            }
        }.onFailure {
            _events.tryEmit(WcEvent.Error(it.message ?: "Approve failed", it))
        }
    }

    override suspend fun previewApprovedChains(
        proposerPublicKey: String,
        walletAccounts: List<Account>
    ): List<String> {
        val proposal = proposalsByPk[proposerPublicKey] ?: return emptyList()

        val supportedNamespaces =
            buildSupportedNamespacesFromWallet(walletAccounts)

        val approved =
            WalletKit.generateApprovedNamespaces(proposal, supportedNamespaces)

        return approved.values
            .flatMap { it.chains ?: emptyList() }
            .distinct()
    }

    private fun buildSupportedNamespacesFromWallet(
        accounts: List<Account>
    ): Map<String, Wallet.Model.Namespace.Session> {

        val grouped = accounts
            .mapNotNull { account ->
                val caip = account.chain.caip ?: return@mapNotNull null
                Triple(
                    caip.namespace,
                    "${caip.namespace}:${caip.chainId}",
                    "${caip.namespace}:${caip.chainId}:${account.address}"
                )
            }
            .groupBy { it.first }

        return grouped.mapValues { (_, entries) ->
            Wallet.Model.Namespace.Session(
                chains = entries.map { it.second }.distinct(),
                accounts = entries.map { it.third }.distinct(),
                methods = listOf(
                    "personal_sign",
                    "eth_sign",
                    "eth_signTypedData",
                    "eth_signTypedData_v4",
                    "eth_sendTransaction"
                ),
                events = listOf("chainChanged", "accountsChanged")
            )
        }
    }

    override suspend fun reject(proposerPublicKey: String, reason: String) {
        val rejectParams = Wallet.Params.SessionReject(proposerPublicKey, reason)
        WalletKit.rejectSession(rejectParams) { error ->
            if (error != null) {
                _events.tryEmit(WcEvent.Error(error.throwable.message ?: "Reject error", error.throwable))
            } else {
                _events.tryEmit(WcEvent.SessionRejected(proposerPublicKey))
            }
        }
    }

    override suspend fun respondResult(topic: String, requestId: Long, result: String) {
        runCatching {
            val jsonRpcResponse = Wallet.Model.JsonRpcResponse.JsonRpcResult(
                id = requestId,
                result = result
            )
            val params = Wallet.Params.SessionRequestResponse(
                sessionTopic = topic,
                jsonRpcResponse = jsonRpcResponse
            )
            WalletKit.respondSessionRequest(params) { error ->
                if (error != null) {
                    _events.tryEmit(WcEvent.Error(error.throwable.message ?: "Respond error", error.throwable))
                }
            }
        }.onFailure {
            _events.tryEmit(WcEvent.Error(it.message ?: "RespondResult failed", it))
        }
    }

    override suspend fun respondError(topic: String, requestId: Long, code: Int, message: String) {
        runCatching {
            val jsonRpcResponse = Wallet.Model.JsonRpcResponse.JsonRpcError(
                id = requestId,
                code = code,
                message = message
            )
            val params = Wallet.Params.SessionRequestResponse(
                sessionTopic = topic,
                jsonRpcResponse = jsonRpcResponse
            )
            WalletKit.respondSessionRequest(params) { error ->
                if (error != null) {
                    _events.tryEmit(WcEvent.Error(error.throwable.message ?: "Respond error", error.throwable))
                }
            }
        }.onFailure {
            _events.tryEmit(WcEvent.Error(it.message ?: "RespondError failed", it))
        }
    }

    override suspend fun disconnect(topic: String) {
        runCatching {
            val params = Wallet.Params.SessionDisconnect(topic)

            WalletKit.disconnectSession(params) { error ->
                if (error != null) {
                    _events.tryEmit(WcEvent.Error(error.throwable.message ?: "Disconnect error", error.throwable))
                } else {
                    _events.tryEmit(WcEvent.Disconnected(topic))
                }
            }
        }.onFailure {
            _events.tryEmit(WcEvent.Error(it.message ?: "Disconnect failed", it))
        }
    }

    override suspend fun disconnectAll() {
        // WalletKit expose parfois une liste de sessions actives selon version.
        // Sinon, il faut tracker les topics depuis onSessionSettleResponse/onSessionDelete.
        _events.tryEmit(WcEvent.Error("disconnectAll not implemented (no active session listing yet)"))
    }
}