package com.cdcoding.datastore.password

import java.io.File
import java.security.SecureRandom
import java.util.*

class DesktopPreferencePasswordStore : PasswordStore {

    private val random = SecureRandom()

    @OptIn(ExperimentalStdlibApi::class)
    override fun createPassword(walletId: String): String {
        val key = ByteArray(16)
        random.nextBytes(key)
        val password = key.toHexString()
        saveToFile(walletId, password)
        return password
    }

    override fun removePassword(walletId: String): Boolean {
        val file = getFile(walletId)
        return if (file.exists()) {
            file.delete()
        } else {
            false
        }
    }

    override fun getPassword(walletId: String): String {
        val file = getFile(walletId)
        if (!file.exists()) {
            throw IllegalAccessError("Password not found")
        }
        return file.readText()
    }

    private fun getFile(walletId: String): File {
        val userHome = System.getProperty("user.home")
        val appDataDir = File(userHome, ".SecureWalletData")
        if (!appDataDir.exists()) {
            appDataDir.mkdirs()
        }
        return File(appDataDir, "$walletId.pwd")
    }

    private fun saveToFile(walletId: String, content: String) {
        val file = getFile(walletId)
        file.writeText(content)
    }
}
