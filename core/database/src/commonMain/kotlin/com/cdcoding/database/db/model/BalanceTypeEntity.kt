package com.cdcoding.database.db.model

import com.cdcoding.model.BalanceType

enum class BalanceTypeEntity(val string: String) {
	available("available"),
	locked("locked"),
	frozen("frozen"),
	staked("staked"),
	pending("pending"),
	rewards("rewards"),
	reserved("reserved"),
}


fun BalanceTypeEntity.asExternal(): BalanceType {
	return when (this) {
		BalanceTypeEntity.available -> BalanceType.available
		BalanceTypeEntity.locked -> BalanceType.locked
		BalanceTypeEntity.frozen -> BalanceType.frozen
		BalanceTypeEntity.staked -> BalanceType.staked
		BalanceTypeEntity.pending -> BalanceType.pending
		BalanceTypeEntity.rewards -> BalanceType.rewards
		BalanceTypeEntity.reserved -> BalanceType.reserved
	}
}


fun BalanceType.asEntity(): BalanceTypeEntity {
	return when (this) {
		BalanceType.available -> BalanceTypeEntity.available
		BalanceType.locked -> BalanceTypeEntity.locked
		BalanceType.frozen -> BalanceTypeEntity.frozen
		BalanceType.staked -> BalanceTypeEntity.staked
		BalanceType.pending -> BalanceTypeEntity.pending
		BalanceType.rewards -> BalanceTypeEntity.rewards
		BalanceType.reserved -> BalanceTypeEntity.reserved
	}
}

