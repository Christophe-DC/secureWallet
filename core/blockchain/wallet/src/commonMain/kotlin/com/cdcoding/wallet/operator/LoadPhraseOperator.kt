package com.cdcoding.wallet.operator

interface LoadPhraseOperator {
    suspend operator fun invoke(walletId: String, password: String): String?
}