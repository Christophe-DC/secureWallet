package com.cdcoding.model.bitcoin

import kotlinx.serialization.Serializable

@Serializable
data class BitcoinUTXO (
	val txid: String,
	val vout: Int,
	val value: String
)

