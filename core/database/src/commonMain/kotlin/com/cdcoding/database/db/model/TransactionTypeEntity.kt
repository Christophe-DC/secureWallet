package com.cdcoding.database.db.model

import com.cdcoding.model.TransactionState
import com.cdcoding.model.TransactionType

enum class TransactionTypeEntity(val string: String) {
	Transfer("transfer"),
	Swap("swap"),
	TokenApproval("tokenApproval");
}


fun TransactionTypeEntity.asExternal(): TransactionType {
	return when (this) {
		TransactionTypeEntity.Transfer -> TransactionType.Transfer
		TransactionTypeEntity.Swap -> TransactionType.Swap
		TransactionTypeEntity.TokenApproval -> TransactionType.TokenApproval
	}
}


fun TransactionType.asEntity(): TransactionTypeEntity {
	return when (this) {
		TransactionType.Transfer -> TransactionTypeEntity.Transfer
		TransactionType.Swap -> TransactionTypeEntity.Swap
		TransactionType.TokenApproval -> TransactionTypeEntity.TokenApproval
	}
}

