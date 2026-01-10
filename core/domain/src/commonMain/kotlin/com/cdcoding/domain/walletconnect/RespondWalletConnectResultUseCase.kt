package com.cdcoding.domain.walletconnect

import com.cdcoding.data.repository.WalletConnectRepository


class RespondWalletConnectResultUseCase(private val repo: WalletConnectRepository) {
    suspend operator fun invoke(topic: String, requestId: Long, result: String) =
        repo.respondResult(topic, requestId, result)
}