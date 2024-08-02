package com.cdcoding.network.model

import kotlinx.serialization.Serializable

@Serializable
data class AssetBalanceDto(
    val assetId: AssetIdDto,
    val balance: BalanceDto,
)
