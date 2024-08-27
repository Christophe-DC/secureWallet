package com.cdcoding.model


data class TransactionExtended (
	val transaction: Transaction,
	val asset: Asset,
	val feeAsset: Asset,
	val price: Price? = null,
	val feePrice: Price? = null,
	val assets: List<Asset>
)

