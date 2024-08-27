package com.cdcoding.securewallet

import android.app.Application
import com.cdcoding.shared.di.initKoin
import org.koin.android.ext.koin.androidContext

class SecureWalletApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@SecureWalletApplication)
        }
        System.loadLibrary("TrustWalletCore")

    }

}

