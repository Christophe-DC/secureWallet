package com.cdcoding.domain.walletconnect

import com.cdcoding.data.repository.SessionRepository
import com.cdcoding.data.repository.WalletConnectRepository

class ApproveWalletConnectProposalUseCase(
    private val repo: WalletConnectRepository,
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke(
        proposerPublicKey: String,
    ) {
        val session = sessionRepository.getSession() ?: error("No session")
        /*val eth = session.wallet.accounts.firstOrNull { it.chain == Chain.Ethereum }
            ?: error("No Ethereum account")

        val caipAccounts = listOf("eip155:1:${eth.address}")*/

        repo.approve(
            proposerPublicKey = proposerPublicKey,
            accounts = session.wallet.accounts
        )
    }
}