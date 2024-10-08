package com.cdcoding.model.sui

import kotlinx.serialization.Serializable

@Serializable
data class SuiCoin (
	val coinType: String,
	val coinObjectId: String,
	val balance: String,
	val version: String,
	val digest: String
)

@Serializable
data class SuiCoinBalance (
	val coinType: String,
	val totalBalance: String
)

@Serializable
data class SuiGasUsed (
	val computationCost: String,
	val storageCost: String,
	val storageRebate: String,
	val nonRefundableStorageFee: String
)

@Serializable
data class SuiStatus (
	val status: String
)

@Serializable
data class SuiObjectReference (
	val objectId: String
)

@Serializable
data class SuiObjectChange (
	val reference: SuiObjectReference
)

@Serializable
data class SuiEffects (
	val gasUsed: SuiGasUsed,
	val status: SuiStatus,
	val created: List<SuiObjectChange>? = null
)

@Serializable
data class SuiTransaction (
	val effects: SuiEffects
)

@Serializable
data class SuiData<T> (
	val data: T
)

