package com.cdcoding.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class EVMChain(val string: String) {
	@SerialName("ethereum")
	Ethereum("ethereum"),
	@SerialName("smartchain")
	SmartChain("smartchain"),
	@SerialName("polygon")
	Polygon("polygon"),
	@SerialName("arbitrum")
	Arbitrum("arbitrum"),
	@SerialName("optimism")
	Optimism("optimism"),
	@SerialName("base")
	Base("base"),
	@SerialName("avalanchec")
	AvalancheC("avalanchec"),
	@SerialName("opbnb")
	OpBNB("opbnb"),
	@SerialName("fantom")
	Fantom("fantom"),
	@SerialName("gnosis")
	Gnosis("gnosis"),
	@SerialName("manta")
	Manta("manta"),
	@SerialName("blast")
	Blast("blast"),
	@SerialName("zksync")
	ZkSync("zksync"),
	@SerialName("linea")
	Linea("linea"),
	@SerialName("mantle")
	Mantle("mantle"),
	@SerialName("celo")
	Celo("celo"),
}

