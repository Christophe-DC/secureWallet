package com.cdcoding.wallet

import wallet.core.jni.HDWallet

actual class WalletClient {
    actual suspend fun createWallet(): Result<String> = Result.success(HDWallet(128, "").mnemonic())
}