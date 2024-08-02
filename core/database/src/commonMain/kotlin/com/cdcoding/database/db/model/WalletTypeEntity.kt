package com.cdcoding.database.db.model

import com.cdcoding.model.WalletType

enum class WalletTypeEntity(val string: String) {
	Multicoin("multicoin"),
	Single("single"),
	View("view");

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
		WalletTypeEntity.Multicoin -> WalletType.multicoin
		WalletTypeEntity.Single -> WalletType.single
		WalletTypeEntity.View -> WalletType.view
	}
}

fun WalletType.asEntity(): WalletTypeEntity {
	return when (this) {
		WalletType.multicoin -> WalletTypeEntity.Multicoin
		WalletType.single -> WalletTypeEntity.Single
		WalletType.view -> WalletTypeEntity.View
	}
}


