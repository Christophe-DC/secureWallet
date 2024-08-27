package com.cdcoding.model

data class ImportType(
    val walletType: WalletType,
    val chain: Chain? = null,
)