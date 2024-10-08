package com.cdcoding.walletdetail.presentation

import com.cdcoding.model.PriceState
import com.cdcoding.model.WalletType

data class WalletInfoUIState(
    val name: String = "",
    val icon: String = "",
    val totalValue: String = "0.0",
    val changedValue: String = "0.0",
    val changedPercentages: String = "0.0%",
    val priceState: PriceState = PriceState.Up,
    val type: WalletType = WalletType.view,
)