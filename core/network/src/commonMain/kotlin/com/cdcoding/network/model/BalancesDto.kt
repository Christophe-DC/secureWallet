package com.cdcoding.network.model

import kotlinx.serialization.Serializable

@Serializable
class BalancesDto(
    val items: List<AssetBalanceDto> = emptyList()
)