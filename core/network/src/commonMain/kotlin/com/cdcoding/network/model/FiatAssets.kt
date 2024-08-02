package com.cdcoding.network.model

import kotlinx.serialization.Serializable

@Serializable
data class FiatAssets (
	val version: UInt,
	val assetIds: List<String>
)

