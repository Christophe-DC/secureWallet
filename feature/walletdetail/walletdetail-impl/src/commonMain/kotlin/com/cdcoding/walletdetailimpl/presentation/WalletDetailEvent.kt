package com.cdcoding.walletdetailimpl.presentation

import com.cdcoding.model.AssetId

sealed interface WalletDetailEvent {
    data object OnRefresh : WalletDetailEvent
    data class HideAsset(val assetId: AssetId) : WalletDetailEvent
}