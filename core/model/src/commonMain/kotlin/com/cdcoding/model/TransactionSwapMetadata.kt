package com.cdcoding.model


data class TransactionSwapMetadata (
	val fromAsset: AssetId,
	val fromValue: String,
	val toAsset: AssetId,
	val toValue: String
)

