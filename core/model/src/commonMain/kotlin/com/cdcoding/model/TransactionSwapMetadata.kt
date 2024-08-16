package com.cdcoding.model

import kotlinx.serialization.Serializable

@Serializable
data class TransactionSwapMetadata (
	val fromAsset: AssetId,
	val fromValue: String,
	val toAsset: AssetId,
	val toValue: String
)

