package com.cdcoding.domain.walletconnect

import com.cdcoding.data.repository.SessionRepository
import com.cdcoding.data.repository.WalletConnectRepository

class PreviewWalletConnectApprovedChainsUseCase(
    private val repo: WalletConnectRepository,
    private val sessionRepository: SessionRepository,
) {
    suspend operator fun invoke(
        proposerPublicKey: String
    ): List<String> {
        val session = sessionRepository.getSession()
        val accounts = session?.wallet?.accounts.orEmpty()
        return repo.previewApprovedChains(proposerPublicKey, accounts)
    }
}
