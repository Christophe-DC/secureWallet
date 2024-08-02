package com.cdcoding.model

import kotlinx.serialization.Serializable

@Serializable
data class Asset (
	val id: AssetId,
	val address: String? = null,
	val name: String,
	val symbol: String,
	val decimals: Int,
	val type: AssetType,
	val assetMetaData: AssetMetaData? = null
)

