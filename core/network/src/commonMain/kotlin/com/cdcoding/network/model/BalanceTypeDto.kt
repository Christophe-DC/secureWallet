package com.cdcoding.network.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class BalanceTypeDto(val string: String) {
	@SerialName("available")
	available("available"),
	@SerialName("locked")
	locked("locked"),
	@SerialName("frozen")
	frozen("frozen"),
	@SerialName("staked")
	staked("staked"),
	@SerialName("pending")
	pending("pending"),
	@SerialName("rewards")
	rewards("rewards"),
	@SerialName("reserved")
	reserved("reserved"),
}

