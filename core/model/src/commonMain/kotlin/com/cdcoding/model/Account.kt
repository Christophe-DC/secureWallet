package com.cdcoding.model

data class Account (
	val id: String,
	val chain: Chain,
	val address: String,
	val derivationPath: String,
	val extendedPublicKey: String? = null
)

