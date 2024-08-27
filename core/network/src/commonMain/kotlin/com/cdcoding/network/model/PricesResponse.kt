package com.cdcoding.network.model

import com.cdcoding.model.AssetPrice
import kotlinx.serialization.Serializable

@Serializable
data class PricesResponse(
    val currency: String,
    val prices: List<AssetPrice> = emptyList()
)