package com.cdcoding.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AssetPrice (
	val assetId: String,
	val price: Double,
	val priceChangePercentage24h: Double
)

@Serializable
data class AssetMarket (
	val marketCap: Double? = null,
	val marketCapRank: Int? = null,
	val totalVolume: Double? = null,
	val circulatingSupply: Double? = null,
	val totalSupply: Double? = null,
	val maxSupply: Double? = null
)

@Serializable
data class AssetPrices (
	val currency: String,
	val prices: List<AssetPrice>
)

@Serializable
data class AssetPricesRequest (
	val currency: String? = null,
	val assetIds: List<String>
)

@Serializable
data class ChartValue (
	val timestamp: Int,
	val value: Float
)

@Serializable
data class Charts (
	val prices: List<ChartValue>,
	val marketCaps: List<ChartValue>,
	val totalVolumes: List<ChartValue>
)

@Serializable
enum class ChartPeriod(val string: String) {
	@SerialName("hour")
	Hour("hour"),
	@SerialName("day")
	Day("day"),
	@SerialName("week")
	Week("week"),
	@SerialName("month")
	Month("month"),
	@SerialName("quarter")
	Quarter("quarter"),
	@SerialName("year")
	Year("year"),
	@SerialName("all")
	All("all"),
}
