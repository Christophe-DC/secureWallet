package com.cdcoding.domain

import com.cdcoding.data.repository.SessionRepository
import com.cdcoding.data.repository.WalletRepository
import com.cdcoding.datastore.password.PasswordStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext


class DeleteWalletUseCase (
    private val sessionRepository: SessionRepository,
    private val walletRepository: WalletRepository,
    private val passwordStore: PasswordStore,
) {

    suspend operator fun invoke(walletId: String, onBoard: () -> Unit) = withContext(Dispatchers.IO) {
        val session = sessionRepository.getSession() ?: return@withContext
        walletRepository.removeWallet(walletId = walletId)
            if (!passwordStore.removePassword(walletId)) {
                return@withContext
            }
        if (session.wallet.id == walletId) {
            val wallets = walletRepository.getAllWallets().firstOrNull() ?: emptyList()
            if (wallets.isEmpty()) {
                sessionRepository.reset()
                withContext(Dispatchers.Main) {
                    onBoard()
                }
            } else {
                sessionRepository.setSession(wallets.first().id)
            }
        }
    }
}