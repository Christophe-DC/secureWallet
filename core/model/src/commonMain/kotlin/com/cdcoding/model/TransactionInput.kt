package com.cdcoding.model

import kotlinx.serialization.Serializable


@Serializable
data class TransactionInput (
	val address: String,
	val value: String
)

