package com.cdcoding.model.cosmos

import com.cdcoding.model.Chain
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class CosmosChain(val string: String) {
	@SerialName("cosmos")
	Cosmos("cosmos"),
	@SerialName("osmosis")
	Osmosis("osmosis"),
	@SerialName("celestia")
	Celestia("celestia"),
	@SerialName("thorchain")
	Thorchain("thorchain"),
	@SerialName("injective")
	Injective("injective"),
	@SerialName("sei")
	Sei("sei"),
	@SerialName("noble")
	Noble("noble"),
}

@Serializable
enum class CosmosDenom(val string: String) {
	@SerialName("rune")
	Rune("rune"),
	@SerialName("uatom")
	Uatom("uatom"),
	@SerialName("uosmo")
	Uosmo("uosmo"),
	@SerialName("utia")
	Utia("utia"),
	@SerialName("inj")
	Inj("inj"),
	@SerialName("usei")
	Usei("usei"),
	@SerialName("uusdc")
	Uusdc("uusdc"),
}


fun CosmosDenom.Companion.from(chain: Chain): String = when (chain) {
	Chain.Cosmos -> CosmosDenom.Uatom.string
	Chain.Osmosis -> CosmosDenom.Uosmo.string
	Chain.Thorchain -> CosmosDenom.Rune.string
	Chain.Celestia -> CosmosDenom.Utia.string
	Chain.Injective -> CosmosDenom.Inj.string
	Chain.Sei -> CosmosDenom.Usei.string
	Chain.Noble -> CosmosDenom.Uusdc.string
	else -> throw IllegalArgumentException("Coin is not supported")
}