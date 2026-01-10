package com.cdcoding.datasource

import com.cdcoding.model.Account
import com.cdcoding.model.WcEvent
import kotlinx.coroutines.flow.Flow

interface WalletConnectDataSource {
    val events: Flow<WcEvent>

    suspend fun pair(uri: String)
    suspend fun approve(
        proposerPublicKey: String,
        accounts: List<Account>
    )

    suspend fun previewApprovedChains(
        proposerPublicKey: String,
        walletAccounts: List<Account>
    ): List<String>
    suspend fun reject(proposerPublicKey: String, reason: String)

    suspend fun respondResult(topic: String, requestId: Long, result: String)
    suspend fun respondError(topic: String, requestId: Long, code: Int, message: String)

    suspend fun disconnect(topic: String)
    suspend fun disconnectAll()
}