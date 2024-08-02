package com.cdcoding.database.mapper


import com.cdcoding.common.utils.toAssetId
import com.cdcoding.database.db.model.asExternal
import com.cdcoding.local.db.AssetEntity
import com.cdcoding.model.Asset
import com.cdcoding.model.AssetId
import com.cdcoding.model.AssetMetaData

fun AssetEntity.asExternal(): Asset? {
    val assetId = this.id.toAssetId() ?: return null
    return Asset(
        id = assetId,
        address = this.address,
        name = this.name,
        symbol = this.symbol,
        decimals = this.decimals.toInt(),
        type = this.type.asExternal(),
        assetMetaData = AssetMetaData(
            isEnabled = this.isVisible,
            isBuyEnabled = this.isBuyEnabled,
            isSwapEnabled = this.isSwapEnabled,
            isStakeEnabled = this.isStakeEnabled
        )
    )
}

/*
fun Asset.asEntity(): AssetEntity {
    return AssetEntity(
        id = this.id.toAssetEntityId(),
        address = this.address,
        name = this.name,
        symbol = this.symbol,
        decimals = this.decimals.toLong(),
        type = this.type.asEntity()
    )
}*/

fun AssetId.toAssetEntityId(): String {
    return "${this.chain.string}_${this.tokenId ?: ""}"
}


