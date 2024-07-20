package com.cdcoding.wallet.client

import com.cdcoding.wallet.model.Account
import com.cdcoding.wallet.model.Chain
import kotlinx.cinterop.ExperimentalForeignApi

actual class WalletClient {

    actual suspend fun createWallet(): String {
        TODO("Not yet implemented")
    }

   // actual suspend fun createWallet(): Result<String> = Result.success(HDWallet(128, "").mnemonic())
    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun createWallet(data: String, chain: Chain): Account {
        //Result.success("HDWalletWrapper.test()"/*HDWalletWrapper.generateMnemonic()*/)
       TODO("Not yet implemented")
   }
}