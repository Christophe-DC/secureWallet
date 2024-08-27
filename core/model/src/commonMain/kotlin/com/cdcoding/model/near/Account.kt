package com.cdcoding.model.near

import kotlinx.serialization.Serializable

@Serializable
data class NearAccount (
	val amount: String
)

@Serializable
data class NearAccountAccessKey (
	val nonce: Long
)

@Serializable
data class NearError (
	val message: String,
	val data: String? = null
)

@Serializable
data class NearRPCError (
	val error: NearError
)

@Serializable
data class NearBlockHeader (
	val hash: String
)

@Serializable
data class NearBlock (
	val header: NearBlockHeader
)

@Serializable
data class NearGasPrice (
	val gas_price: String
)

@Serializable
data class NearBroadcastTransaction (
	val hash: String
)

@Serializable
data class NearBroadcastResult (
	val final_execution_status: String,
	val transaction: NearBroadcastTransaction
)

