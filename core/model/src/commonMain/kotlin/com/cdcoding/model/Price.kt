package com.cdcoding.model

import kotlinx.serialization.Serializable

@Serializable
data class Price (
	val price: Double,
	val priceChangePercentage24h: Double
)

