package com.cdcoding.database.db.model

import com.cdcoding.model.TransactionType

enum class TransactionTypeEntity(val string: String) {
	Transfer("transfer"),
	Swap("swap"),
	TokenApproval("tokenApproval"),
	StakeUndelegate("stakeUndelegate"),
	StakeRewards("stakeRewards"),
	StakeRedelegate("stakeRedelegate"),
	StakeWithdraw("stakeWithdraw"),
	StakeDelegate("stakeDelegate");
}


fun TransactionTypeEntity.asExternal(): TransactionType {
	return when (this) {
		TransactionTypeEntity.Transfer -> TransactionType.Transfer
		TransactionTypeEntity.Swap -> TransactionType.Swap
		TransactionTypeEntity.TokenApproval -> TransactionType.TokenApproval
		TransactionTypeEntity.StakeUndelegate -> TransactionType.StakeUndelegate
		TransactionTypeEntity.StakeRewards -> TransactionType.StakeRewards
		TransactionTypeEntity.StakeRedelegate -> TransactionType.StakeRedelegate
		TransactionTypeEntity.StakeWithdraw -> TransactionType.StakeWithdraw
		TransactionTypeEntity.StakeDelegate -> TransactionType.StakeDelegate
	}
}


fun TransactionType.asEntity(): TransactionTypeEntity {
	return when (this) {
		TransactionType.Transfer -> TransactionTypeEntity.Transfer
		TransactionType.Swap -> TransactionTypeEntity.Swap
		TransactionType.TokenApproval -> TransactionTypeEntity.TokenApproval
		TransactionType.StakeUndelegate -> TransactionTypeEntity.StakeUndelegate
		TransactionType.StakeRewards -> TransactionTypeEntity.StakeRewards
		TransactionType.StakeRedelegate -> TransactionTypeEntity.StakeRedelegate
		TransactionType.StakeWithdraw -> TransactionTypeEntity.StakeWithdraw
		TransactionType.StakeDelegate -> TransactionTypeEntity.StakeDelegate
	}
}

