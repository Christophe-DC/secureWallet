package com.cdcoding.database.mapper


import com.cdcoding.database.db.model.asEntity
import com.cdcoding.database.db.model.asExternal
import com.cdcoding.common.utils.toAssetId
import com.cdcoding.local.db.BalanceEntity
import com.cdcoding.model.AssetBalance
import com.cdcoding.model.Balance

fun BalanceEntity.asExternal(): AssetBalance? {
    val assetId = this.assetId.toAssetId() ?: return null
    return AssetBalance(
        assetId = assetId,
        balance = Balance(
            type = this.type.asExternal(),
            value = this.amount
        )
    )
}

fun Balance.asEntity(assetId: String, address: String): BalanceEntity {
    return BalanceEntity(
        assetId = assetId,
        address = address,
        type = this.type.asEntity(),
        amount = this.value,
        updatedAt = null
    )
}
