package com.cdcoding.model.cosmos

import kotlinx.serialization.Serializable

@Serializable
data class CosmosBalance (
	val denom: String,
	val amount: String
)

@Serializable
data class CosmosBalances (
	val balances: List<CosmosBalance>
)

