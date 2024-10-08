package com.cdcoding.model.tron

import kotlinx.serialization.Serializable

@Serializable
data class TronHeader (
	val number: Long,
	val version: Long,
	val txTrieRoot: String,
	val witness_address: String,
	val parentHash: String,
	val timestamp: Long
)

@Serializable
data class TronHeaderRawData (
	val raw_data: TronHeader
)

@Serializable
data class TronBlock (
	val block_header: TronHeaderRawData
)

