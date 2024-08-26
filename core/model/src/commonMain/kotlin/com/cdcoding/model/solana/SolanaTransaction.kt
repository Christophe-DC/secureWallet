package com.cdcoding.model.solana

import kotlinx.serialization.Serializable

@Serializable
object SolanaTransactionError

@Serializable
data class SolanaTransactionMeta (
	val err: SolanaTransactionError? = null
)

@Serializable
data class SolanaTransaction (
	val meta: SolanaTransactionMeta,
	val slot: Int
)

