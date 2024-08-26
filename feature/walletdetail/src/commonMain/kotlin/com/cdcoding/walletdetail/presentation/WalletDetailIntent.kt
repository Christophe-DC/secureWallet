package com.cdcoding.walletdetail.presentation

import com.cdcoding.model.AssetId

sealed interface WalletDetailIntent {
    data object OnRefresh : WalletDetailIntent
}