package com.cdcoding.common.utils

import java.io.File

class DesktopDataDir(): DataDir {
    override val path: String
        get() {
            val userHome = System.getProperty("user.home")
            val appDataDir = File(userHome, ".SecureWalletData")
            if (!appDataDir.exists()) {
                appDataDir.mkdirs()
            }
            return appDataDir.absolutePath
        }
}
