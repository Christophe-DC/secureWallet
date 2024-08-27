package com.cdcoding.model.tron

import kotlinx.serialization.Serializable

@Serializable
data class TronTransactionBroadcast (
	val result: Boolean,
	val txid: String
)

@Serializable
data class TronTransactionBroadcastError (
	val message: String
)

@Serializable
data class TronTransactionContractRef (
	val contractRet: String
)

@Serializable
data class TronTransaction (
	val ret: List<TronTransactionContractRef>
)

@Serializable
data class TronReceipt (
	val result: String? = null
)

@Serializable
data class TronTransactionReceipt (
	val blockNumber: Int,
	val fee: Int? = null,
	val result: String? = null,
	val receipt: TronReceipt? = null
)

