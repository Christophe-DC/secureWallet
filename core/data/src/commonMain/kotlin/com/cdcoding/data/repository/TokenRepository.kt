package com.cdcoding.data.repository

import com.cdcoding.database.db.TokenDao
import com.cdcoding.model.Asset
import com.cdcoding.model.AssetFull
import com.cdcoding.model.AssetId
import com.cdcoding.model.AssetInfo
import com.cdcoding.model.AssetScore
import com.cdcoding.model.Chain
import com.cdcoding.network.client.GemApiClient
import com.cdcoding.network.util.getOrNull
import kotlinx.coroutines.flow.Flow


interface TokenRepository {
    suspend fun getByIds(ids: List<AssetId>): List<Asset>

    suspend fun getByChains(chains: List<Chain>): List<Asset>

    suspend fun search(query: String)

    suspend fun search(assetId: AssetId)

    suspend fun search(chains: List<Chain>, query: String): Flow<List<Asset>>

    suspend fun assembleAssetInfo(assetId: AssetId): AssetInfo?
}

class DefaultTokenRepository(
    private val tokenDao: TokenDao,
    private val gemApiClient: GemApiClient,
    //private val getTokenClients: List<GetTokenClient>
) : TokenRepository {

    override suspend fun getByIds(ids: List<AssetId>): List<Asset> = tokenDao.getByIds(ids)

    override suspend fun getByChains(chains: List<Chain>): List<Asset> =
        tokenDao.getByChains(chains)

    override suspend fun search(query: String) {

        val tokens = try {
            val result = gemApiClient.search(query)
            result.getOrNull()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
        if (tokens.isNullOrEmpty()) {
            val assets = null /*getTokenClients.map {
                try {
                    if (it.isTokenQuery(query)) {
                        it.getTokenData(query)
                    } else {
                        null
                    }
                } catch (err: Throwable) {
                    null
                }

            }
                .mapNotNull { it }.map {
                    AssetFull(
                        asset = it,
                        score = AssetScore(0),
                    )
                }
            tokenDao.addTokens(assets)*/
        } else {
            tokenDao.addTokens(tokens.filter { it.asset.id != null })
        }
    }


    override suspend fun search(assetId: AssetId) {
        val tokenId = assetId.tokenId ?: return
        val asset: Asset? = null /* getTokenClients
            .firstOrNull { it.isMaintain(assetId.chain) && it.isTokenQuery(tokenId) }
            ?.getTokenData(tokenId)*/
        if (asset == null) {
            search(tokenId)
            return
        }
        tokenDao.addTokens(listOf(AssetFull(asset, score = AssetScore(0))))
    }

    override suspend fun search(chains: List<Chain>, query: String): Flow<List<Asset>> {
        return tokenDao.search(chains = chains, query)
    }

    override suspend fun assembleAssetInfo(assetId: AssetId): AssetInfo? {
        return tokenDao.assembleAssetInfo(assetId)
    }
}