package com.cdcoding.model

enum class WalletType(val string: String) {
	multicoin("multicoin"),
	single("single"),
	view("view"),
}

data class Wallet (
	val id: String,
	val name: String,
	val index: Long,
	val type: WalletType,
	val accounts: List<Account>
)

