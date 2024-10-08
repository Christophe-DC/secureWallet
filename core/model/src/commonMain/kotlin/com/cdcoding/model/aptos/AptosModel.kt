package com.cdcoding.model.aptos

import kotlinx.serialization.Serializable

@Serializable
data class AptosResource<T> (
	val type: String,
	val data: T
)

@Serializable
data class AptosResourceCoin (
	val value: String
)

@Serializable
data class AptosResourceBalance (
	val coin: AptosResourceCoin
)

@Serializable
data class AptosAccount (
	val sequence_number: String
)

@Serializable
data class AptosTransaction (
	val success: Boolean
)

@Serializable
data class AptosTransactionBroacast (
	val hash: String
)

@Serializable
data class AptosGasFee (
	val gas_estimate: Int,
	val prioritized_gas_estimate: Int
)

