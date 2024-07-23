package com.cdcoding.data.local.db.model

import com.cdcoding.model.Chain
import com.cdcoding.model.WalletType

enum class WalletTypeEntity(val string: String) {
	multicoin("multicoin"),
	single("single"),
	view("view");

	companion object {
		operator fun invoke(string: String): ChainEntity? {
			return fromRaw(string)
		}

		private fun fromRaw(string: String): ChainEntity? {
			return ChainEntity.entries.find { it.string == string }
		}
	}
}


fun WalletTypeEntity.asExternal(): WalletType {
	return when (this) {
		WalletTypeEntity.multicoin -> WalletType.multicoin
		WalletTypeEntity.single -> WalletType.single
		WalletTypeEntity.view -> WalletType.view
	}
}

fun WalletType.asEntity(): WalletTypeEntity {
	return when (this) {
		WalletType.multicoin -> WalletTypeEntity.multicoin
		WalletType.single -> WalletTypeEntity.single
		WalletType.view -> WalletTypeEntity.view
	}
}


