package com.cdcoding.data.repository

import com.cdcoding.datasource.WalletConnectDataSource
import com.cdcoding.model.Account
import com.cdcoding.model.WcEvent
import com.cdcoding.model.WcNamespaceRequest
import kotlinx.coroutines.flow.Flow

interface WalletConnectRepository {
    val events: Flow<WcEvent>

    suspend fun pair(uri: String)

    suspend fun approve(
        proposerPublicKey: String,
        accounts: List<Account>
    )

    suspend fun previewApprovedChains(
        proposerPublicKey: String,
        walletAccounts: List<Account>,
        requestedNamespaces: Map<String, WcNamespaceRequest>
    ): List<String>

    suspend fun reject(proposerPublicKey: String, reason: String)

    suspend fun respondResult(topic: String, requestId: Long, result: String) // result = "0x..."
    suspend fun respondError(topic: String, requestId: Long, code: Int, message: String)

    suspend fun disconnect(topic: String)
    suspend fun disconnectAll()
}

class WalletConnectRepositoryImpl(
    private val ds: WalletConnectDataSource
) : WalletConnectRepository {

    override val events: Flow<WcEvent> = ds.events

    override suspend fun pair(uri: String) = ds.pair(uri)
    override suspend fun approve(
        proposerPublicKey: String,
        accounts: List<Account>
    ) = ds.approve(proposerPublicKey,  accounts)

    override suspend fun previewApprovedChains(
        proposerPublicKey: String,
        walletAccounts: List<Account>,
        requestedNamespaces: Map<String, WcNamespaceRequest>
    ) = ds.previewApprovedChains(proposerPublicKey,  walletAccounts, requestedNamespaces)

    override suspend fun reject(proposerPublicKey: String, reason: String) =
        ds.reject(proposerPublicKey, reason)

    override suspend fun respondResult(topic: String, requestId: Long, result: String) =
        ds.respondResult(topic, requestId, result)

    override suspend fun respondError(topic: String, requestId: Long, code: Int, message: String) =
        ds.respondError(topic, requestId, code, message)

    override suspend fun disconnect(topic: String) = ds.disconnect(topic)
    override suspend fun disconnectAll() = ds.disconnectAll()
}