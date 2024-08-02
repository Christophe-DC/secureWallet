package com.cdcoding.walletdetailimpl.presentation

import com.cdcoding.model.Session
import com.cdcoding.model.TransactionExtended
import com.cdcoding.model.WalletType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList


data class WalletDetailUIState(
    val isLoading: Boolean = false,
    val session: Session? = null,
    val walletInfo: WalletInfoUIState = WalletInfoUIState(),
    val assets: ImmutableList<AssetUIState> = emptyList<AssetUIState>().toImmutableList(),
    val pendingTransactions: ImmutableList<TransactionExtended> = emptyList<TransactionExtended>().toImmutableList(),
    val swapEnabled: Boolean = true,
    val error: String = "",
)