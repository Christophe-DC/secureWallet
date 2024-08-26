package com.cdcoding.model

import com.cdcoding.model.serializer.InstantAsLongSerializer
import com.cdcoding.model.serializer.JsonAsStringSerializer
import kotlinx.serialization.Serializable

@Serializable
data class TransactionsFetchOption (
	val wallet_index: Int,
	val asset_id: String? = null,
	val from_timestamp: UInt? = null
)

@Serializable
data class Transaction (
	val id: String,
	val hash: String,
	val assetId: AssetId,
	val from: String,
	val to: String,
	val contract: String? = null,
	val type: TransactionType,
	val state: TransactionState,
	val blockNumber: String,
	val sequence: String,
	val fee: String,
	val feeAssetId: AssetId,
	val value: String,
	val memo: String? = null,
	val direction: TransactionDirection,
	val utxoInputs: List<TransactionInput>,
	val utxoOutputs: List<TransactionInput>,
	@Serializable(with = JsonAsStringSerializer::class)
	val metadata: String? = null,
	@Serializable(with = InstantAsLongSerializer::class)
	val createdAt: Long
)

