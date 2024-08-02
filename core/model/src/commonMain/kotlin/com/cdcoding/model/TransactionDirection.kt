package com.cdcoding.model


enum class TransactionDirection(val string: String) {
	SelfTransfer("self"),
	Outgoing("outgoing"),
	Incoming("incoming"),
}

