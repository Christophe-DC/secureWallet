package com.cdcoding.model.cosmos

import kotlinx.serialization.Serializable

@Serializable
data class CosmosBroadcastResult (
	val txhash: String,
	val code: Int,
	val raw_log: String
)

@Serializable
data class CosmosBroadcastResponse (
	val tx_response: CosmosBroadcastResult
)

@Serializable
data class CosmosTransactionDataResponse (
	val txhash: String,
	val code: Int
)

@Serializable
data class CosmosTransactionResponse (
	val tx_response: CosmosTransactionDataResponse
)

