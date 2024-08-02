package com.cdcoding.database.db.model

import com.cdcoding.model.AssetType

enum class AssetTypeEntity(val string: String) {
	NATIVE("NATIVE"),
	ERC20("ERC20"),
	BEP20("BEP20"),
	SPL("SPL"),
	TRC20("TRC20"),
	TOKEN("TOKEN"),
	IBC("IBC"),
	JETTON("JETTON"),
	SYNTH("SYNTH"),
}


fun AssetTypeEntity.asExternal(): AssetType {
	return when (this) {
		AssetTypeEntity.NATIVE -> AssetType.NATIVE
		AssetTypeEntity.ERC20 -> AssetType.ERC20
		AssetTypeEntity.BEP20 -> AssetType.BEP20
		AssetTypeEntity.SPL -> AssetType.SPL
		AssetTypeEntity.TRC20 -> AssetType.TRC20
		AssetTypeEntity.TOKEN -> AssetType.TOKEN
		AssetTypeEntity.IBC -> AssetType.IBC
		AssetTypeEntity.JETTON -> AssetType.JETTON
		AssetTypeEntity.SYNTH -> AssetType.SYNTH
	}
}


fun AssetType.asEntity(): AssetTypeEntity {
	return when (this) {
		AssetType.NATIVE -> AssetTypeEntity.NATIVE
		AssetType.ERC20 -> AssetTypeEntity.ERC20
		AssetType.BEP20 -> AssetTypeEntity.BEP20
		AssetType.SPL -> AssetTypeEntity.SPL
		AssetType.TRC20 -> AssetTypeEntity.TRC20
		AssetType.TOKEN -> AssetTypeEntity.TOKEN
		AssetType.IBC -> AssetTypeEntity.IBC
		AssetType.JETTON -> AssetTypeEntity.JETTON
		AssetType.SYNTH -> AssetTypeEntity.SYNTH
	}
}

