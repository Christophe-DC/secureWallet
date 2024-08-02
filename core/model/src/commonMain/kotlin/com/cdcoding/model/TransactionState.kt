package com.cdcoding.model

enum class TransactionState(val string: String) {
	Pending("pending"),
	Confirmed("confirmed"),
	Failed("failed"),
	Reverted("reverted"),
}

