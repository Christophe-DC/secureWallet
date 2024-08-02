package com.cdcoding.database.mapper


import com.cdcoding.common.utils.toAssetId
import com.cdcoding.database.db.model.asExternal
import com.cdcoding.local.db.TokenEntity
import com.cdcoding.model.Asset

fun TokenEntity.toAsset(): Asset? {
    val assetId = this.id.toAssetId() ?: return null
    return Asset(
        id = assetId,
        name = this.name,
        symbol = this.symbol,
        decimals = this.decimals.toInt(),
        type = this.type.asExternal()
    )
}


