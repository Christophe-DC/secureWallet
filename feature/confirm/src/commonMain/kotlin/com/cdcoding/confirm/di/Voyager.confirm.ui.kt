package com.cdcoding.confirm.di

import cafe.adriel.voyager.core.registry.screenModule
import com.cdcoding.confirm.ui.ConfirmScreen
import com.cdcoding.core.navigation.ConfirmDestination


val confirmScreenModule = screenModule {
    register<ConfirmDestination.Confirm> { provider ->
        ConfirmScreen(
            confirmParams = provider.confirmParams,
            txType = provider.txType
        )
    }
}
