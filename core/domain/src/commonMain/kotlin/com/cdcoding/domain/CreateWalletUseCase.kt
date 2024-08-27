package com.cdcoding.domain

import com.cdcoding.model.Wallet
import com.cdcoding.data.repository.WalletRepository

class CreateWalletUseCase(
    private val walletRepository: WalletRepository,
) {

    suspend operator fun invoke(walletName: String): Result<Wallet> = walletRepository.createWallet(walletName)
}