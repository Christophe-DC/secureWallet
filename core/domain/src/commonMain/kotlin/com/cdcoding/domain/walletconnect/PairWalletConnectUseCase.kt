package com.cdcoding.domain.walletconnect

import com.cdcoding.data.repository.WalletConnectRepository

class PairWalletConnectUseCase(private val repo: WalletConnectRepository) {
    suspend operator fun invoke(uri: String) = repo.pair(uri)
}