package com.cdcoding.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class AssetType(val string: String) {
	@SerialName("NATIVE")
	NATIVE("NATIVE"),
	@SerialName("ERC20")
	ERC20("ERC20"),
	@SerialName("BEP20")
	BEP20("BEP20"),
	@SerialName("SPL")
	SPL("SPL"),
	@SerialName("TRC20")
	TRC20("TRC20"),
	@SerialName("TOKEN")
	TOKEN("TOKEN"),
	@SerialName("IBC")
	IBC("IBC"),
	@SerialName("JETTON")
	JETTON("JETTON"),
	@SerialName("SYNTH")
	SYNTH("SYNTH"),
}

@Serializable
enum class AssetSubtype(val string: String) {
	@SerialName("NATIVE")
	NATIVE("NATIVE"),
	@SerialName("TOKEN")
	TOKEN("TOKEN"),
}