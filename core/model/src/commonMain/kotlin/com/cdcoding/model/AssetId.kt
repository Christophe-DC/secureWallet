package com.cdcoding.model
import kotlinx.serialization.Serializable

@Serializable
data class AssetId (
	val chain: Chain,
	val tokenId: String? = null
)

