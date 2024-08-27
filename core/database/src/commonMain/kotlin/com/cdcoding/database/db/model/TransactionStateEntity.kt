package com.cdcoding.database.db.model

import com.cdcoding.model.TransactionState

enum class TransactionStateEntity(val string: String) {
	Pending("pending"),
	Confirmed("confirmed"),
	Failed("failed"),
	Reverted("reverted"),
}


fun TransactionStateEntity.asExternal(): TransactionState {
	return when (this) {
		TransactionStateEntity.Pending -> TransactionState.Pending
		TransactionStateEntity.Confirmed -> TransactionState.Confirmed
		TransactionStateEntity.Failed -> TransactionState.Failed
		TransactionStateEntity.Reverted -> TransactionState.Reverted
	}
}


fun TransactionState.asEntity(): TransactionStateEntity {
	return when (this) {
		TransactionState.Pending -> TransactionStateEntity.Pending
		TransactionState.Confirmed -> TransactionStateEntity.Confirmed
		TransactionState.Failed -> TransactionStateEntity.Failed
		TransactionState.Reverted -> TransactionStateEntity.Reverted
	}
}

