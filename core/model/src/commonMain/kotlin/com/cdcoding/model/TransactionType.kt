package com.cdcoding.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class TransactionType(val string: String) {
	@SerialName("transfer")
	Transfer("transfer"),
	@SerialName("swap")
	Swap("swap"),
	@SerialName("tokenApproval")
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