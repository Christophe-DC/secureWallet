package com.cdcoding.network.model

import kotlinx.serialization.Serializable

@Serializable
data class AssetIdDto (
	val chain: ChainDto,
	val tokenId: String? = null
)

