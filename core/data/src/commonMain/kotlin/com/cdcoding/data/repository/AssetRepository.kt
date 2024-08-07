package com.cdcoding.data.repository

import com.cdcoding.common.utils.toIdentifier
import com.cdcoding.database.db.AssetDao
import com.cdcoding.database.db.BalanceDao
import com.cdcoding.database.db.PriceDao
import com.cdcoding.database.mapper.toAssetEntityId
import com.cdcoding.model.Account
import com.cdcoding.model.Asset
import com.cdcoding.model.AssetBalance
import com.cdcoding.model.AssetId
import com.cdcoding.model.AssetInfo
import com.cdcoding.model.AssetMetaData
import com.cdcoding.model.AssetPrice
import com.cdcoding.model.AssetPriceInfo
import com.cdcoding.model.Balance
import com.cdcoding.model.Balances
import com.cdcoding.model.Currency
import com.cdcoding.model.Wallet
import com.cdcoding.network.client.BalancesRemoteSource
import com.cdcoding.network.client.GemApiClient
import com.cdcoding.network.util.getOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

interface AssetRepository {
    suspend fun getById(accounts: List<Account>, ids: List<AssetId>): Result<List<AssetInfo>>
    suspend fun getById(accounts: List<Account>, assetId: AssetId): Result<List<AssetInfo>>
    fun getAllByWalletFlow(wallet: Wallet): Flow<List<AssetInfo>>
    suspend fun syncTokens(wallet: Wallet, currency: Currency)
    fun addAsset(asset: Asset, address: String? = null, isVisible: Boolean? = null)
    fun addAssets(assets: List<Asset>, address: String?)
    suspend fun setVisibility(account: Account, assetId: AssetId, visibility: Boolean)
    suspend fun updateBalances(account: Account, tokens: List<AssetId>): List<Balances>
    suspend fun updatePrices(currency: Currency, vararg assetIds: AssetId)
}

class DefaultAssetRepository(
    private val assetDao: AssetDao,
    private val balanceDao: BalanceDao,
    private val pricesDao: PriceDao,
    private val balancesRemoteSource: BalancesRemoteSource,
    private val gemApiClient: GemApiClient
) : AssetRepository {

    override suspend fun syncTokens(wallet: Wallet, currency: Currency) =
        withContext(Dispatchers.IO) {
            val balancesJob = async(Dispatchers.IO) {
                updateBalances(wallet)
            }
            val pricesJob = async(Dispatchers.IO) {
                updatePrices(currency)
            }
            balancesJob.await()
            pricesJob.await()
        }

    override suspend fun getById(
        accounts: List<Account>,
        assetId: AssetId
    ): Result<List<AssetInfo>> {
        return getById(accounts, listOf(assetId))
    }

    override suspend fun getById(
        accounts: List<Account>,
        ids: List<AssetId>
    ): Result<List<AssetInfo>> {
        val roomAssetId = ids.map { it.toIdentifier() }
        val addresses = accounts.map { it.address }.toSet().toList()
        val assets = assetDao.getAssetsById(addresses, roomAssetId).filterNotNull()
        if (assets.isEmpty()) {
            return Result.failure(Exception("Asset doesn't found"))
        }
        val balances =
            balanceDao.getBalancesByAssetId(addresses, roomAssetId).firstOrNull()?.filterNotNull()
        val prices =
            pricesDao.getPricesById(assets.map { it.id.toIdentifier() }).filterNotNull().map {
                AssetPriceInfo(
                    price = AssetPrice(
                        it.assetId,
                        it.price,
                        it.priceChangePercentage24h
                    ), currency = Currency.USD
                )
            }
        return Result.success(
            assets.mapNotNull { asset ->
                val assetId = asset.id
                val account =
                    accounts.firstOrNull { it.address == asset.address && it.chain == assetId.chain }
                        ?: return@mapNotNull null
                AssetInfo(
                    owner = account,
                    asset = Asset(
                        id = assetId,
                        name = asset.name,
                        symbol = asset.symbol,
                        decimals = asset.decimals,
                        type = asset.type,
                    ),
                    balances = Balances(
                        balances?.map {
                            AssetBalance(
                                assetId,
                                Balance(value = it.balance.value, type = it.balance.type)
                            )
                        } ?: emptyList()
                    ),
                    price = prices.firstOrNull { it.price.assetId == asset.id.toIdentifier() },
                    metadata = AssetMetaData(
                        isEnabled = asset.assetMetaData?.isEnabled ?: false,
                        isBuyEnabled = asset.assetMetaData?.isBuyEnabled ?: false,
                        isSwapEnabled = asset.assetMetaData?.isSwapEnabled ?: false,
                        isStakeEnabled = asset.assetMetaData?.isStakeEnabled ?: false,
                    ),
                    links = null, // if (asset.links != null) gson.fromJson(asset.links, AssetLinks::class.java) else null,
                    market = null, // if (room.market != null) gson.fromJson(asset.market, AssetMarket::class.java) else null,
                    rank = 0
                )
            }
        )
    }


    private fun getAllByAccounts(accounts: List<Account>, query: String): Flow<List<AssetInfo>> {
        val addresses = accounts.map { it.address }.toSet().toList()
        val chains = accounts.map { it.chain }
        val assetsFlow = assetDao.getAssetsByOwner(addresses, query)
        val balancesFlow = balanceDao.getBalancesByOwner(addresses)
        val pricesFlow = pricesDao.prices()

        return combine(assetsFlow, balancesFlow, pricesFlow) { assets, balances, allPrices ->
            val prices = allPrices.map {
                if (it == null) return@map null
                AssetPriceInfo(price = it, currency = Currency.USD)
            }.filterNotNull()
            assets.mapNotNull { asset ->
                val assetId = asset?.id ?: return@mapNotNull null
                if (!chains.contains(assetId.chain)) {
                    return@mapNotNull null
                }
                val account =
                    accounts.firstOrNull { it.address == asset.address && it.chain == assetId.chain }
                        ?: return@mapNotNull null

                AssetInfo(
                    owner = account,
                    asset = asset,
                    balances = Balances(balances.filter { balance -> balance?.assetId == asset.id }
                        .filterNotNull()),
                    price = prices.firstOrNull { it.price.assetId == asset.id.toAssetEntityId() },
                    /*links = if (room.links != null) gson.fromJson(
                        room.links,
                        AssetLinks::class.java
                    ) else null,
                    market = if (room.market != null) gson.fromJson(
                        room.market,
                        AssetMarket::class.java
                    ) else null,
                    rank = room.rank*/
                )
            }
        }
    }

    override fun getAllByWalletFlow(wallet: Wallet): Flow<List<AssetInfo>> {
        return getAllByAccounts(wallet.accounts, "")
    }

    override fun addAsset(asset: Asset, address: String?, isVisible: Boolean?) {
        assetDao.insertAsset(asset, address, isVisible)
    }

    override fun addAssets(assets: List<Asset>, address: String?) {
        assetDao.insertAssets(assets, address)
    }

    override suspend fun setVisibility(account: Account, assetId: AssetId, visibility: Boolean) {
        val asset = assetDao.getAssetsById(listOf(account.address), listOf(assetId.toIdentifier()))
            .firstOrNull() ?: return
        assetDao.insertAsset(asset.copy(assetMetaData = asset.assetMetaData?.copy(isEnabled = visibility)))
    }

    private suspend fun updateBalances(wallet: Wallet) {
        wallet.accounts.forEach { account ->

            val assets = getAllByAccounts(listOf(account), "")
                .firstOrNull()
                ?.filter { it.metadata?.isEnabled == true }
                ?.map { it.asset.id } ?: return@forEach
            if (assets.isEmpty()) {
                return@forEach
            }
            updateBalances(account, assets)
            // onRefreshAssets.forEach { it() }
        }
    }

    override suspend fun updateBalances(account: Account, tokens: List<AssetId>): List<Balances> {
        val balances =
            balancesRemoteSource.getBalances(account, tokens).getOrNull() ?: return emptyList()
        // assetsLocalSource.setBalances(account, balances)
        return balances
    }

    override suspend fun updatePrices(currency: Currency, vararg assetIds: AssetId) = withContext(Dispatchers.IO) {
        val ids = if (assetIds.isEmpty()) {
            assetDao.getAssets().mapNotNull { it?.id }.toSet().toList()
        } else {
            assetIds.toList()
        }
        val prices = gemApiClient.getPrices(currency.string, ids).getOrNull()?.prices //?: return@withContext
        if (prices != null) {
            pricesDao.setPrices(prices)
        }
    }
}