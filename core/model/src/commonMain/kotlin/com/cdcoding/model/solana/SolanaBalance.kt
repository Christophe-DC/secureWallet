package com.cdcoding.model.solana

import kotlinx.serialization.Serializable

@Serializable
data class SolanaBalance (
	val value: Long
)

@Serializable
data class SolanaBalanceValue (
	val amount: String
)

