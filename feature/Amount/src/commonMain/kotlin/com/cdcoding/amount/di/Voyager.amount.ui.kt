package com.cdcoding.amount.di

import cafe.adriel.voyager.core.registry.screenModule
import com.cdcoding.amount.ui.AmountScreen
import com.cdcoding.core.navigation.AmountDestination

val amountScreenModule = screenModule {
    register<AmountDestination.Amount> { provider ->
        AmountScreen(
            assetId = provider.assetId,
            destinationAddress = provider.destinationAddress,
            addressDomain = provider.addressDomain,
            memo = provider.memo,
        )
    }
}
