package com.cdcoding.wallet.operator

interface StorePhraseOperator {
    suspend operator fun invoke(
        walletId: String,
        mnemonic: String,
        password: String): Result<Boolean>
}