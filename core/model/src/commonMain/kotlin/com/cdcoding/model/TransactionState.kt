package com.cdcoding.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class TransactionState(val string: String) {
	@SerialName("pending")
	Pending("pending"),
	@SerialName("confirmed")
	Confirmed("confirmed"),
	@SerialName("failed")
	Failed("failed"),
	@SerialName("reverted")
	Reverted("reverted"),
}

