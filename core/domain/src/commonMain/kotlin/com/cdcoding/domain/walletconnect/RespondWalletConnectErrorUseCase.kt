package com.cdcoding.domain.walletconnect

import com.cdcoding.data.repository.WalletConnectRepository

class RespondWalletConnectErrorUseCase(private val repo: WalletConnectRepository) {
    suspend operator fun invoke(topic: String, requestId: Long, code: Int, message: String) =
        repo.respondError(topic, requestId, code, message)
}