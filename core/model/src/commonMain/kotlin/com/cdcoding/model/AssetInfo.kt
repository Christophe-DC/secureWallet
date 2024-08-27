package com.cdcoding.model

data class AssetInfo(
    val owner: Account,
    val asset: Asset,
    val balances: Balances = Balances(),
    val walletType: WalletType = WalletType.view,
    val walletName: String = "",
    val price: AssetPriceInfo? = null,
    val metadata: AssetMetaData? = null,
    val links: AssetLinks? = null,
    val market: AssetMarket? = null,
    val rank: Int = 0,
    val stakeApr: Double? = null,
)
