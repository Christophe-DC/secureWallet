package com.cdcoding.domain

import com.cdcoding.wallet.model.Wallet
import com.cdcoding.wallet.repository.WalletRepository

class GetCreateWalletUseCase(
    private val walletRepository: WalletRepository,
) {

    suspend operator fun invoke(walletName: String): Result<Wallet> = walletRepository.createWallet(walletName)
}