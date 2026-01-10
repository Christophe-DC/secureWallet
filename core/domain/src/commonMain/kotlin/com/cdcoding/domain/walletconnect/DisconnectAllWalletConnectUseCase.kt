package com.cdcoding.domain.walletconnect

import com.cdcoding.data.repository.WalletConnectRepository

class DisconnectAllWalletConnectUseCase(private val repo: WalletConnectRepository) {
    suspend operator fun invoke() = repo.disconnectAll()
}