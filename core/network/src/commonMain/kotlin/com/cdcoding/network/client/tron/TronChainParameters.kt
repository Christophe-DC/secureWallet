package com.cdcoding.network.client.tron

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TronChainParameter (
	val key: String,
	val value: Long? = null
)

@Serializable
data class TronChainParameters (
	val chainParameter: List<TronChainParameter>
)

@Serializable
enum class TronChainParameterKey(val string: String) {
	@SerialName("getCreateNewAccountFeeInSystemContract")
	getCreateNewAccountFeeInSystemContract("getCreateNewAccountFeeInSystemContract"),
	@SerialName("getCreateAccountFee")
	getCreateAccountFee("getCreateAccountFee"),
	@SerialName("getEnergyFee")
	getEnergyFee("getEnergyFee"),
}

