package com.cdcoding.wallet.client

import com.cdcoding.wallet.model.Account
import com.cdcoding.wallet.model.Chain

actual class WalletClient actual constructor() {
    actual suspend fun createWallet(): String {
        TODO("Not yet implemented")
    }

    actual suspend fun createWallet(data: String, chain: Chain): Account {
        TODO("Not yet implemented")
    }
}