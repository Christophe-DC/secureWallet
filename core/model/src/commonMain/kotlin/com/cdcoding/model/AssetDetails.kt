package com.cdcoding.model

import kotlinx.serialization.Serializable

@Serializable
data class AssetDetails (
	val links: AssetLinks,
	val isBuyable: Boolean,
	val isSellable: Boolean,
	val isSwapable: Boolean,
	val isStakeable: Boolean,
	val stakingApr: Double? = null
)