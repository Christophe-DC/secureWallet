package com.cdcoding.model

import kotlinx.serialization.Serializable

@Serializable
data class AssetFull (
	val asset: Asset,
	val details: AssetDetails? = null,
	val price: Price? = null,
	val market: AssetMarket? = null,
	val score: AssetScore
)