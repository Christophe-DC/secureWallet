package com.cdcoding.model.ethereum

import kotlinx.serialization.Serializable

@Serializable
data class EthereumTransactionReciept (
	val status: String,
	val gasUsed: String,
	val effectiveGasPrice: String,
	val l1Fee: String? = null
)

@Serializable
data class EthereumFeeHistory (
	val reward: List<List<String>>,
	val baseFeePerGas: List<String>
)

