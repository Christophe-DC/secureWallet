package com.cdcoding.model.ton

import kotlinx.serialization.Serializable

@Serializable
data class TonWalletInfo (
	val seqno: Int? = null,
	val last_transaction_id: TonTransactionId
)

@Serializable
data class TonResult<T> (
	val result: T
)

