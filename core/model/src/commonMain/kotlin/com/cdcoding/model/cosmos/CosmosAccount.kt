package com.cdcoding.model.cosmos

import kotlinx.serialization.Serializable

@Serializable
data class CosmosAccount (
	val account_number: String,
	val sequence: String
)

@Serializable
data class CosmosAccountResponse<T> (
	val account: T
)

@Serializable
data class CosmosInjectiveAccount (
	val base_account: CosmosAccount
)

