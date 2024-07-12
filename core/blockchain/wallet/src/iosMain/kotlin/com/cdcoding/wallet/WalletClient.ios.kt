package com.cdcoding.wallet

//import cocoapods.WalletCore.*
//import cocoapods.wallet_wrapper.HDWalletWrapper
import kotlinx.cinterop.*
import platform.Foundation.*
import platform.UIKit.*


actual class WalletClient {

   // actual suspend fun createWallet(): Result<String> = Result.success(HDWallet(128, "").mnemonic())
    @OptIn(ExperimentalForeignApi::class)
    actual suspend fun createWallet(): Result<String> = Result.success("HDWalletWrapper.test()"/*HDWalletWrapper.generateMnemonic()*/)
}
