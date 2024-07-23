package com.cdcoding.wallet.client

import com.cdcoding.model.Account
import com.cdcoding.model.Chain

expect class WalletClient() {
    suspend fun createWallet(): String
    suspend fun createWallet(data: String, chain: Chain): Account
}