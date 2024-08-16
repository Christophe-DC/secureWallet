package com.cdcoding.model.tron

import kotlinx.serialization.Serializable

@Serializable
data class TronAccountRequest (
	val address: String,
	val visible: Boolean
)

@Serializable
data class TronAccount (
	val balance: Long? = null,
	val address: String? = null
)

@Serializable
data class TronAccountUsage (
	val freeNetUsed: Int? = null,
	val freeNetLimit: Int? = null
)

@Serializable
data class TronEmptyAccount (
	val address: String? = null
)

