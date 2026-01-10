package com.cdcoding.domain.walletconnect

import com.cdcoding.data.repository.WalletConnectRepository

class RejectWalletConnectProposalUseCase(private val repo: WalletConnectRepository) {
    suspend operator fun invoke(proposerPublicKey: String, reason: String) = repo.reject(proposerPublicKey, reason)
}