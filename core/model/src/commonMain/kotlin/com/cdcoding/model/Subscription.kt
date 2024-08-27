package com.cdcoding.model

import kotlinx.serialization.*

@Serializable
data class Subscription (
	@SerialName("wallet_index")
	val walletIndex: Int,
	val chain: Chain,
	val address: String
)

