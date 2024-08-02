package com.cdcoding.database.mapper


import com.cdcoding.local.db.PriceEntity
import com.cdcoding.model.AssetPrice

fun PriceEntity.asExternal(): AssetPrice {
    return AssetPrice(
        assetId = this.assetId,
        price = this.price,
        priceChangePercentage24h = this.dayChanged
    )
}

fun AssetPrice.asEntity(): PriceEntity {
    return PriceEntity(
        assetId = this.assetId,
        price = this.price,
        dayChanged = this.priceChangePercentage24h
    )
}
