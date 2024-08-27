package com.cdcoding.model.solana

import kotlinx.serialization.Serializable

@Serializable
data class SolanaBlockhash (
	val blockhash: String
)

@Serializable
data class SolanaBlockhashResult (
	val value: SolanaBlockhash
)

