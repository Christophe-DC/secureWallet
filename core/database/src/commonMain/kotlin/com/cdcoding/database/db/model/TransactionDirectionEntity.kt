package com.cdcoding.database.db.model

import com.cdcoding.model.TransactionDirection

enum class TransactionDirectionEntity(val string: String) {
	SelfTransfer("self"),
	Outgoing("outgoing"),
	Incoming("incoming"),
}


fun TransactionDirectionEntity.asExternal(): TransactionDirection {
	return when (this) {
		TransactionDirectionEntity.SelfTransfer -> TransactionDirection.SelfTransfer
		TransactionDirectionEntity.Outgoing -> TransactionDirection.Outgoing
		TransactionDirectionEntity.Incoming -> TransactionDirection.Incoming
	}
}


fun TransactionDirection.asEntity(): TransactionDirectionEntity {
	return when (this) {
		TransactionDirection.SelfTransfer -> TransactionDirectionEntity.SelfTransfer
		TransactionDirection.Outgoing -> TransactionDirectionEntity.Outgoing
		TransactionDirection.Incoming -> TransactionDirectionEntity.Incoming
	}
}

