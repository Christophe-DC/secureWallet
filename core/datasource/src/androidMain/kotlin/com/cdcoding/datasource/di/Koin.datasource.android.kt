package com.cdcoding.datasource.di


import com.cdcoding.datasource.WalletConnectDataSource
import com.cdcoding.walletconnect.datasource.WalletConnectDataSourceAndroid
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module {
    return module {
        single<WalletConnectDataSource> {
            WalletConnectDataSourceAndroid(
                application = get(),
                projectId = "b8df353b8940de1245297c5ea1c93ceb",
            )
        }
    }
}
