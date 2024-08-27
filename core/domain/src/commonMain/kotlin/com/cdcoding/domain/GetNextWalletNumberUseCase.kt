package com.cdcoding.domain

import com.cdcoding.data.repository.WalletRepository

class GetNextWalletNumberUseCase(
    private val walletRepository: WalletRepository,
) {

    suspend operator fun invoke(): Int = walletRepository.getNextWalletNumber()
}