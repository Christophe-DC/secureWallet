package com.cdcoding.receiveasset.presentation

import com.cdcoding.model.Chain

data class ReceiveAssetState(
    val isLoading: Boolean = true,
    val walletName: String = "",
    val address: String = "",
    val assetTitle: String = "",
    val assetSymbol: String = "",
    val chain: Chain? = null,
    val errorMessage: String? = null,
)