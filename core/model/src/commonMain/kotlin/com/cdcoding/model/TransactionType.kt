package com.cdcoding.model


enum class TransactionType(val string: String) {
	Transfer("transfer"),
	Swap("swap"),
	TokenApproval("tokenApproval");
}


fun TransactionType.getValue(direction: TransactionDirection, value: String): String {
	return when (this) {
		/*TransactionType.StakeUndelegate,
		TransactionType.StakeRewards,
		TransactionType.StakeRedelegate,
		TransactionType.StakeWithdraw -> value
		TransactionType.StakeDelegate -> value*/
		TransactionType.Transfer,
		TransactionType.Swap -> when (direction) {
			TransactionDirection.SelfTransfer,
			TransactionDirection.Outgoing -> "-${value}"
			TransactionDirection.Incoming -> "+${value}"
		}
		TransactionType.TokenApproval -> ""
	}
}