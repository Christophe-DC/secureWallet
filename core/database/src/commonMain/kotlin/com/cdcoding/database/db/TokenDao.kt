package com.cdcoding.database.db


import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.cdcoding.common.utils.toIdentifier
import com.cdcoding.database.db.model.asEntity
import com.cdcoding.database.mapper.AssembleAssetInfoMapper
import com.cdcoding.database.mapper.toAsset
import com.cdcoding.database.mapper.toAssetEntityId
import com.cdcoding.local.db.SecureWalletDatabase
import com.cdcoding.model.Asset
import com.cdcoding.model.AssetFull
import com.cdcoding.model.AssetId
import com.cdcoding.model.AssetInfo
import com.cdcoding.model.AssetType
import com.cdcoding.model.Chain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface TokenDao {
    suspend fun addTokens(tokens: List<AssetFull>)

    suspend fun getByIds(ids: List<AssetId>): List<Asset>

    suspend fun getByChains(chains: List<Chain>): List<Asset>

    suspend fun search(chains: List<Chain>, query: String): Flow<List<Asset>>

    suspend fun assembleAssetInfo(assetId: AssetId): AssetInfo?
}

class DefaultTokenDao(
    database: SecureWalletDatabase
) : TokenDao {

    private val tokenQueries = database.tokenQueries

    override suspend fun addTokens(tokens: List<AssetFull>) {
        tokenQueries.transaction {
            tokens.forEach { token ->
                tokenQueries.insertToken(
                    id = token.asset.id.toAssetEntityId(),
                    name = token.asset.name,
                    symbol = token.asset.symbol,
                    decimals = token.asset.decimals.toLong(),
                    type = token.asset.type.asEntity(),
                    rank = token.score.rank.toLong(),
                )
            }

        }
    }

    override suspend fun getByIds(ids: List<AssetId>): List<Asset> {
        return tokenQueries.getTokensById(ids.map { it.toIdentifier() }).executeAsList().mapNotNull { it.toAsset() }
    }

    override suspend fun getByChains(chains: List<Chain>): List<Asset> {
        return  tokenQueries.getTokensByType( chains.mapNotNull {  chain -> getTokenType(chain)?.asEntity() }).executeAsList().mapNotNull { it.toAsset() }
    }

    override suspend fun search(chains: List<Chain>, query: String): Flow<List<Asset>> {
        return  tokenQueries.search( chains.mapNotNull {  chain -> getTokenType(chain)?.asEntity() }, query).asFlow()
            .mapToList(Dispatchers.IO)
            .map { tokenEntities ->
                tokenEntities.mapNotNull { tokenEntity ->
                    tokenEntity.toAsset()
                }
            }
    }

    override suspend fun assembleAssetInfo(assetId: AssetId): AssetInfo? {
        val dbAssetInfo = tokenQueries.assembleAssetInfo(assetId.chain.asEntity(), assetId.toIdentifier())

       // val dbAssetInfo = tokensDao.assembleAssetInfo(assetId.chain, assetId.toIdentifier())
        return AssembleAssetInfoMapper().asExternal(dbAssetInfo.executeAsList()).firstOrNull()
    }

    private fun getTokenType(chain: Chain) = when (chain) {
        Chain.SmartChain -> AssetType.BEP20
        Chain.Base,
        Chain.AvalancheC,
        Chain.Polygon,
        Chain.Arbitrum,
        Chain.OpBNB,
        Chain.Manta,
        Chain.Fantom,
        Chain.Gnosis,
        Chain.Optimism,
        Chain.Blast,
        Chain.ZkSync,
        Chain.Linea,
        Chain.Mantle,
        Chain.Celo,
        Chain.Ethereum -> AssetType.ERC20
        Chain.Solana -> AssetType.SPL
        Chain.Tron -> AssetType.TRC20
        Chain.Sui -> AssetType.TOKEN
        Chain.Ton -> AssetType.JETTON
        Chain.Cosmos,
        Chain.Osmosis,
        Chain.Celestia,
        Chain.Thorchain,
        Chain.Injective,
        Chain.Noble,
        Chain.Sei -> AssetType.IBC
        Chain.Bitcoin,
        Chain.Litecoin,
        Chain.Doge,
        Chain.Aptos,
        Chain.Near,
        Chain.Xrp -> null
    }
}
