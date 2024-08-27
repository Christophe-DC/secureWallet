package com.cdcoding.model

import kotlinx.serialization.Serializable

@Serializable
data class AssetMetaData (
	val isEnabled: Boolean = true,
	val isBuyEnabled: Boolean = false,
	val isSwapEnabled: Boolean = false,
	val isStakeEnabled: Boolean = false
)

