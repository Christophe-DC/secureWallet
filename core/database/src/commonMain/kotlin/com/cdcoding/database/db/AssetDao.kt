package com.cdcoding.database.db


import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.cdcoding.database.db.model.asEntity
import com.cdcoding.database.mapper.asExternal
import com.cdcoding.database.mapper.toAssetEntityId
import com.cdcoding.local.db.SecureWalletDatabase
import com.cdcoding.model.Asset
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock

interface AssetDao {
    fun assets(): Flow<List<Asset?>>
    fun getAssets(): List<Asset?>
    fun getAssetsById(addresses: List<String>, assetId: List<String>): List<Asset?>
    fun getAssetsById(assetId: String): List<Asset?>
    fun getAssetsByOwner(addresses: List<String>, query: String): Flow<List<Asset?>>
    fun insertAsset(asset: Asset, address: String? = null, isVisible: Boolean? = null)
    fun insertAssets(assets: List<Asset>, address: String?)
}

class DefaultAssetDao(
    database: SecureWalletDatabase
) : AssetDao {

    private val assetQueries = database.assetQueries

    override fun assets(): Flow<List<Asset?>> = assetQueries.getAssets().asFlow()
        .mapToList(Dispatchers.IO)
        .map { assetEntities ->
            assetEntities.map { assetEntity ->
                assetEntity.asExternal()
            }
        }

    override fun getAssets(): List<Asset?> {
        return assetQueries.getAssets().executeAsList()
            .map { it.asExternal() }
    }

    override fun getAssetsById(addresses: List<String>, assetId: List<String>): List<Asset?> {
        return assetQueries.getAssetsByAddressAndId(addresses, assetId).executeAsList()
            .map { it.asExternal() }
    }

    override fun getAssetsById(assetId: String): List<Asset?> {
        return assetQueries.getAssetsById(assetId).executeAsList().map { it.asExternal() }
    }

    override fun getAssetsByOwner(addresses: List<String>, query: String): Flow<List<Asset?>> {
        return assetQueries.getAssetsByOwner(addresses, query).asFlow().mapToList(Dispatchers.IO)
            .map { assetEntities ->
                assetEntities.map { assetEntity ->
                    assetEntity.asExternal()
                }
            }
    }

    override fun insertAsset(asset: Asset, address: String?, isVisible: Boolean?) {
        assetQueries.transaction {
            val assetAddress = address ?: asset.address ?: return@transaction
            val assetVisibility = isVisible ?: asset.assetMetaData?.isEnabled ?: true
            assetQueries.insertAsset(
                id = asset.id.toAssetEntityId(),
                address = assetAddress,
                name = asset.name,
                symbol = asset.symbol,
                decimals = asset.decimals.toLong(),
                type = asset.type.asEntity(),
                isVisible = assetVisibility,
                isBuyEnabled = asset.assetMetaData?.isBuyEnabled ?: false,
                isSwapEnabled = asset.assetMetaData?.isSwapEnabled ?: false,
                isStakeEnabled = asset.assetMetaData?.isStakeEnabled ?: false,
                stakingApr = 0.0,
                links = "links",
                market = "market",
                rank = 1,
                createdAt = Clock.System.now().epochSeconds,
                updatedAt = Clock.System.now().epochSeconds
            )
        }
    }

    override fun insertAssets(assets: List<Asset>, address: String?) {
        assetQueries.transaction {
            assets.forEach { asset ->
                val assetAddress = address ?: asset.address ?: return@transaction
                assetQueries.insertAsset(
                    id = asset.id.toAssetEntityId(),
                    address = assetAddress,
                    name = asset.name,
                    symbol = asset.symbol,
                    decimals = asset.decimals.toLong(),
                    type = asset.type.asEntity(),
                    isVisible = asset.assetMetaData?.isEnabled ?: true,
                    isBuyEnabled = asset.assetMetaData?.isBuyEnabled ?: false,
                    isSwapEnabled = asset.assetMetaData?.isSwapEnabled ?: false,
                    isStakeEnabled = asset.assetMetaData?.isStakeEnabled ?: false,
                    stakingApr = 0.0,
                    links = "links",
                    market = "market",
                    rank = 1,
                    createdAt = Clock.System.now().epochSeconds,
                    updatedAt = Clock.System.now().epochSeconds
                )
            }
        }
    }
}
