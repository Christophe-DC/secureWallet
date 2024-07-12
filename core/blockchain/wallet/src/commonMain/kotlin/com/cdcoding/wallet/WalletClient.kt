package com.cdcoding.wallet

expect class WalletClient() {
    suspend fun createWallet(): Result<String>
}