package com.cdcoding.wallet.client

interface StorePhraseClient {
    suspend operator fun invoke(
        walletId: String,
        mnemonic: String,
        password: String): Result<Boolean>
}