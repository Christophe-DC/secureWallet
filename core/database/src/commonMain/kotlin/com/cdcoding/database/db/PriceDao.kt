package com.cdcoding.database.db


import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.cdcoding.database.mapper.asExternal
import com.cdcoding.local.db.SecureWalletDatabase
import com.cdcoding.model.AssetPrice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface PriceDao {
    fun prices(): Flow<List<AssetPrice?>>
    fun getPrices(): List<AssetPrice?>
    fun getPricesById(assetsId: List<String>): List<AssetPrice?>
    fun setPrice(price: AssetPrice)
    fun setPrices(prices: List<AssetPrice>)
    fun deletePrices()
}

class DefaultPriceDao(
    database: SecureWalletDatabase
) : PriceDao {

    private val priceQueries = database.priceQueries

    override fun prices(): Flow<List<AssetPrice?>> = priceQueries.getPrices().asFlow()
        .mapToList(Dispatchers.IO)
        .map { priceEntities ->
            priceEntities.map { priceEntity ->
                priceEntity.asExternal()
            }
        }

    override fun getPrices(): List<AssetPrice?> {
        return priceQueries.getPrices().executeAsList().map { it.asExternal() }
    }

    override fun getPricesById(assetsId: List<String>): List<AssetPrice?> {
        return priceQueries.getPricesByAssets(assetsId).executeAsList().map { it.asExternal() }
    }

    override fun setPrice(price: AssetPrice) {
        priceQueries.transaction {
            priceQueries.insertPrice(
                assetId = price.assetId,
                price = price.price,
                dayChanged = price.priceChangePercentage24h
            )
        }
    }

    override fun setPrices(prices: List<AssetPrice>) {
        priceQueries.transaction {
            prices.forEach { price ->
                priceQueries.insertPrice(
                    assetId = price.assetId,
                    price = price.price,
                    dayChanged = price.priceChangePercentage24h
                )
            }
        }
    }

    override fun deletePrices() {
        priceQueries.transaction {
            priceQueries.deletePrices()
        }
    }
}
