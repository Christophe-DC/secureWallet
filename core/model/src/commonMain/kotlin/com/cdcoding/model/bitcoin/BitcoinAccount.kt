package com.cdcoding.model.bitcoin

import kotlinx.serialization.Serializable

@Serializable
data class BitcoinAccount (
	val balance: String
)

