package com.cdcoding.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NameRecord (
	val name: String,
	val chain: Chain,
	val address: String,
	val provider: String
)

@Serializable
enum class NameProvider(val string: String) {
	@SerialName("ud")
	Ud("ud"),
	@SerialName("ens")
	Ens("ens"),
	@SerialName("sns")
	Sns("sns"),
	@SerialName("ton")
	Ton("ton"),
	@SerialName("tree")
	Tree("tree"),
	@SerialName("spaceid")
	Spaceid("spaceid"),
	@SerialName("eths")
	Eths("eths"),
	@SerialName("did")
	Did("did"),
	@SerialName("suins")
	Suins("suins"),
	@SerialName("aptos")
	Aptos("aptos"),
	@SerialName("injective")
	Injective("injective"),
	@SerialName("icns")
	Icns("icns"),
	@SerialName("lens")
	Lens("lens"),
	@SerialName("bns")
	Bns("bns"),
}

