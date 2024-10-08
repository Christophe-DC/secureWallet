package com.cdcoding.model.ton


import kotlinx.serialization.Serializable

@Serializable
data class TonTransactionId (
	val hash: String
)

@Serializable
data class TonTransaction (
	val transaction_id: TonTransactionId
)

@Serializable
data class TonTransactionMessage (
	val hash: String
)

@Serializable
data class TonJettonTokenContentData (
	val name: String,
	val symbol: String,
	val decimals: String
)

@Serializable
data class TonJettonTokenContent (
	val type: String,
	val data: TonJettonTokenContentData
)

@Serializable
data class TonJettonToken (
	val jetton_content: TonJettonTokenContent
)

@Serializable
data class TonJettonBalance (
	val balance: Long
)

