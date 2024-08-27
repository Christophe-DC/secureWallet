package com.cdcoding.network.client

import com.cdcoding.model.Account
import com.cdcoding.model.TransactionType


class BroadcastProxy(
    private val clients: List<BroadcastClient>,
) {
    suspend fun broadcast(account: Account, message: ByteArray, type: TransactionType): Result<String> {
        return clients.firstOrNull { it.isMaintain(account.chain) }
            ?.send(account, message, type) ?: Result.failure(Exception())
    }
}