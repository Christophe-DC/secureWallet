package com.cdcoding.wallet.client

import com.cdcoding.wallet.model.Account
import com.cdcoding.wallet.model.Chain

expect class WalletClient() {
    suspend fun createWallet(): String
    suspend fun createWallet(data: String, chain: Chain): Account
}