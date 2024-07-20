package com.cdcoding.wallet.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class WalletType(val string: String) {
	@SerialName("multicoin")
	multicoin("multicoin"),
	@SerialName("single")
	single("single"),
	@SerialName("view")
	view("view"),
}

@Serializable
data class Wallet (
	val id: String,
	val name: String,
	val index: Long,
	val type: WalletType,
	val accounts: List<Account>
)

