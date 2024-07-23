package com.cdcoding.domain

import com.cdcoding.wallet.repository.WalletRepository

class GetNextWalletNumberUseCase(
    private val walletRepository: WalletRepository,
) {

    suspend operator fun invoke(): Int = walletRepository.getNextWalletNumber()
}