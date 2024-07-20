
package com.cdcoding.wallet.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Chain(val string: String) {
	@SerialName("bitcoin")
	Bitcoin("bitcoin"),
	@SerialName("litecoin")
	Litecoin("litecoin"),
	@SerialName("ethereum")
	Ethereum("ethereum"),
	@SerialName("smartchain")
	SmartChain("smartchain"),
	@SerialName("solana")
	Solana("solana"),
	@SerialName("polygon")
	Polygon("polygon"),
	@SerialName("thorchain")
	Thorchain("thorchain"),
	@SerialName("cosmos")
	Cosmos("cosmos"),
	@SerialName("osmosis")
	Osmosis("osmosis"),
	@SerialName("arbitrum")
	Arbitrum("arbitrum"),
	@SerialName("ton")
	Ton("ton"),
	@SerialName("tron")
	Tron("tron"),
	@SerialName("doge")
	Doge("doge"),
	@SerialName("optimism")
	Optimism("optimism"),
	@SerialName("aptos")
	Aptos("aptos"),
	@SerialName("base")
	Base("base"),
	@SerialName("avalanchec")
	AvalancheC("avalanchec"),
	@SerialName("sui")
	Sui("sui"),
	@SerialName("xrp")
	Xrp("xrp"),
	@SerialName("opbnb")
	OpBNB("opbnb"),
	@SerialName("fantom")
	Fantom("fantom"),
	@SerialName("gnosis")
	Gnosis("gnosis"),
	@SerialName("celestia")
	Celestia("celestia"),
	@SerialName("injective")
	Injective("injective"),
	@SerialName("sei")
	Sei("sei"),
	@SerialName("manta")
	Manta("manta"),
	@SerialName("blast")
	Blast("blast"),
	@SerialName("noble")
	Noble("noble"),
	@SerialName("zksync")
	ZkSync("zksync"),
	@SerialName("linea")
	Linea("linea"),
	@SerialName("mantle")
	Mantle("mantle"),
	@SerialName("celo")
	Celo("celo"),
	@SerialName("near")
	Near("near"),
}

