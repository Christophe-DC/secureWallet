package com.cdcoding.model.solana

import kotlinx.serialization.Serializable

@Serializable
data class SolanaValue<T> (
	val value: T
)

