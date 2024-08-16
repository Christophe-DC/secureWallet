package com.cdcoding.datastore.password

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.get
import kotlinx.cinterop.memScoped
import platform.CoreCrypto.kCCSuccess
import platform.Foundation.NSUserDefaults
import platform.Security.SecRandomCopyBytes
import platform.Security.kSecRandomDefault


class IOSPreferencePasswordStore() : PasswordStore {

    private val userDefaults = NSUserDefaults.standardUserDefaults()

    @OptIn(ExperimentalStdlibApi::class)
    override fun createPassword(walletId: String): String {
        val key = generateSecureRandomBytes(16)
        val password = key.toHexString()
        userDefaults.setObject(password, forKey = walletId)
        userDefaults.synchronize()

        return password
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun generateSecureRandomBytes(size: Int): ByteArray {
        return memScoped {
            val bytes = allocArray<ByteVar>(size)
            val result = SecRandomCopyBytes(kSecRandomDefault, size.toULong(), bytes)
            if (result != kCCSuccess) {
                throw IllegalStateException("Failed to generate secure random bytes")
            }
            ByteArray(size) { index -> bytes[index] }
        }
    }

    override fun getPassword(walletId: String): String = userDefaults.stringForKey(walletId) ?: ""

    override fun removePassword(walletId: String): Boolean {
        userDefaults.removeObjectForKey(walletId)
        return true
    }

}