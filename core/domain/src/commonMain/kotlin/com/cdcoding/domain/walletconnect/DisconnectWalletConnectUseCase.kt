package com.cdcoding.domain.walletconnect

import com.cdcoding.data.repository.WalletConnectRepository

class DisconnectWalletConnectUseCase(private val repo: WalletConnectRepository) {
    suspend operator fun invoke(topic: String) = repo.disconnect(topic)
}