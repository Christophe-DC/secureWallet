package com.cdcoding.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class TransactionDirection(val string: String) {
	@SerialName("self")
	SelfTransfer("self"),
	@SerialName("outgoing")
	Outgoing("outgoing"),
	@SerialName("incoming")
	Incoming("incoming"),
}

