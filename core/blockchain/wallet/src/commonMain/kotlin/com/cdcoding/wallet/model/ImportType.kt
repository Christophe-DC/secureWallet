package com.cdcoding.wallet.model

data class ImportType(
    val walletType: WalletType,
    val chain: Chain? = null,
)