package com.cdcoding.model

enum class BalanceType(val string: String) {
	available("available"),
	locked("locked"),
	frozen("frozen"),
	staked("staked"),
	pending("pending"),
	rewards("rewards"),
	reserved("reserved"),
}

