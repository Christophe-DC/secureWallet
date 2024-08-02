package com.cdcoding.data.repository

import com.cdcoding.common.utils.ChainInfoLocalSource
import com.cdcoding.common.utils.asset
import com.cdcoding.common.utils.getAccount
import com.cdcoding.common.utils.toAssetId
import com.cdcoding.common.utils.toIdentifier
import com.cdcoding.common.utils.uuid4
import com.cdcoding.database.db.SessionDao
import com.cdcoding.database.db.WalletDao
import com.cdcoding.datastore.ConfigRepository
import com.cdcoding.model.Account
import com.cdcoding.model.AssetId
import com.cdcoding.model.Chain
import com.cdcoding.model.Currency
import com.cdcoding.model.ImportType
import com.cdcoding.model.Subscription
import com.cdcoding.model.Wallet
import com.cdcoding.model.WalletType
import com.cdcoding.network.client.GemApiClient
import com.cdcoding.network.util.getOrNull
import com.cdcoding.wallet.client.WalletClient
import com.cdcoding.wallet.validator.InvalidPhrase
import com.cdcoding.wallet.validator.InvalidWords
import com.cdcoding.wallet.validator.PhraseValidator
import com.ionspin.kotlin.bignum.integer.BigInteger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

class WalletRepository(
    private val walletsDao: WalletDao,
    private val sessionDao: SessionDao,
    private val assetRepository: AssetRepository,
    private val walletClient: WalletClient,
    private val phraseValidator: PhraseValidator,
    private val gemApiClient: GemApiClient,
    private val configRepository: ConfigRepository,
    private val tokensRepository: TokenRepository,
) {

    private val visibleByDefault =
        listOf(Chain.Ethereum, Chain.Bitcoin, Chain.SmartChain, Chain.Solana)

    suspend fun getNextWalletNumber(): Int {
        return (walletsDao.getAllWallets().firstOrNull()?.size ?: 0) + 1
    }

    suspend fun getAllWallets() = walletsDao.getAllWallets()

    suspend fun insertWallet(walletName: String, address: String, chain: Chain): Result<Unit> {
        return runCatching {
            val walletId = uuid4()
            /*walletsDao.insertWalletWithAccount(
                wallet = WalletEntity(
                    id = walletId,
                    name = walletName,
                    wallet_index = getNextWalletNumber().toLong(),
                    type = WalletType.multicoin,
                ),
                accounts = listOf(
                    AccountEntity(
                        id = uuid4(),
                        address = address,
                        chain = chain.name,
                        derivationPath = "",
                        extendedPublicKey = null
                    )
                )
            )*/
        }
    }

    suspend fun createWallet(walletName: String): Result<Wallet> {
        val phrase = walletClient.createWallet()
        val result = handlePhrase(ImportType(WalletType.multicoin), walletName, phrase)
        if (result.isFailure) {
            return result
        }
        val wallet = result.getOrNull() ?: return Result.failure(Exception("Unknown error"))
        syncSubscription()
        invalidateDefault(wallet.type, wallet, sessionDao.getSession()?.currency ?: Currency.USD)
        sessionDao.setWallet(wallet)
        return Result.success(wallet)
    }

    private suspend fun handlePhrase(
        importType: ImportType,
        walletName: String,
        rawData: String
    ): Result<Wallet> {
        val cleanedData = rawData.trim().split("\\s+".toRegex()).joinToString(" ") { it.trim() }
        val validateResult = phraseValidator(cleanedData)
        if (validateResult.isFailure || validateResult.getOrNull() != true) {
            val error = validateResult.exceptionOrNull() ?: InvalidPhrase
            return when (error) {
                is InvalidWords -> Result.failure(ImportError.InvalidWords(error.words))
                else -> Result.failure(ImportError.InvalidationSecretPhrase)
            }
        }
        val result = addPhrase(walletName, cleanedData, importType.walletType, importType.chain)
        val wallet = result.getOrNull()
        return if (result.isFailure || wallet == null) {
            result
        } else {
            result
            /*val password = passwordStore.createPassword(wallet.id)
            val storeResult = storePhraseOperator(wallet.id, cleanedData, password)
            if (storeResult.isSuccess) {
                result
            } else {
                walletsRepository.removeWallet(wallet.id)
                Result.failure(storeResult.exceptionOrNull() ?: ImportError.CreateError("Unknown error"))
            }*/
        }
    }


    suspend fun addPhrase(
        walletName: String,
        data: String,
        type: WalletType,
        chain: Chain?
    ): Result<Wallet> = withContext(Dispatchers.IO) {
        val accounts = mutableListOf<Account>()
        val chains =
            if (type == WalletType.single && chain != null) listOf(chain) else Chain.entries
        for (item in chains) {
            accounts.add(walletClient.createWallet(data, item))
        }

        val wallet = Wallet(
            id = uuid4(),
            name = walletName,
            index = getNextWalletNumber().toLong(),
            type = WalletType.multicoin,
            accounts = accounts
        )

        walletsDao.insertWalletWithAccount(wallet, accounts)
        Result.success(wallet)
    }

    suspend fun updateWallet(wallet: Wallet) {
        // walletsLocalSource.updateWallet(wallet)
    }

    // suspend fun removeWallet(walletId: String) = walletsLocalSource.removeWallet(walletId)

    // suspend fun getWallet(walletId: String): Result<Wallet> = walletsLocalSource.getWallet(walletId)


    suspend fun invalidateDefault(walletType: WalletType, wallet: Wallet, currency: Currency) = withContext(Dispatchers.IO) {
        val assets = assetRepository.getAllByWalletFlow(wallet).firstOrNull()
            ?.associateBy({ it.asset.id.toIdentifier() }, { it }) ?: emptyMap()
        wallet.accounts.filter { !ChainInfoLocalSource.exclude.contains(it.chain) }
            .map { account ->
                Pair(account, account.chain.asset())
            }.map {
                val isNew = assets[it.first.chain.string] == null
                val isVisible = assets[it.second.id.toIdentifier()]?.metadata?.isEnabled
                        ?: visibleByDefault.contains(it.first.chain) || walletType != WalletType.multicoin
                assetRepository.addAsset(it.second, it.first.address, isVisible)

                async {
                    if (isNew) {
                        val balances =
                            assetRepository.updateBalances(it.first, emptyList()).firstOrNull()
                        if ((balances?.calcTotal()?.atomicValue?.compareTo(BigInteger.ZERO)
                                ?: 0) > 0
                        ) {
                            assetRepository.setVisibility(it.first, it.second.id, true)
                        }
                    }

                }
            }.awaitAll()
        delay(2000) // Wait subscription
        val availableAssets =
            gemApiClient.getAssets(configRepository.getDeviceId(), wallet.index.toInt()).getOrNull()
                ?: return@withContext
        availableAssets.mapNotNull {
            it.toAssetId()
        }.filter {
            it.tokenId != null
        }.map { assetId ->
            async {
                val account = wallet.getAccount(assetId.chain) ?: return@async
                tokensRepository.search(assetId.tokenId!!)
                switchVisibility(account, assetId, true, currency)
            }
        }.awaitAll()
    }

    suspend fun switchVisibility(
        owner: Account,
        assetId: AssetId,
        visibility: Boolean,
        currency: Currency,
    ) = withContext(Dispatchers.IO) {
        val assetResult = assetRepository.getById(listOf(owner), assetId)
        if (assetResult.isFailure || assetResult.getOrNull()?.isEmpty() != false) {
            val asset = tokensRepository.getByIds(listOf(assetId))
            assetRepository.addAssets(asset, owner.address)
        }
        assetRepository.setVisibility(owner, assetId, visibility)
        assetRepository.updateBalances(owner, listOf(assetId))
        //updatePrices(currency)
        //onRefreshAssets.forEach { it() }
    }

    private suspend fun syncSubscription() {
        val deviceId = configRepository.getDeviceId()
        val wallets = walletsDao.getAllWallets().firstOrNull() ?: return
        val subscriptionsIndex = mutableMapOf<String, Subscription>()

        wallets.forEach { wallet ->
            wallet.accounts.forEach { account ->
                subscriptionsIndex["${account.chain.string}_${account.address}_${wallet.index}"] = Subscription(
                    chain = account.chain,
                    address = account.address,
                    walletIndex = wallet.index.toInt(),
                )
            }
        }

        val result = gemApiClient.getSubscriptions(deviceId)
        val remoteSubscriptions = result.getOrNull() ?: emptyList()
        remoteSubscriptions.forEach {
            subscriptionsIndex.remove("${it.chain.string}_${it.address}_${it.walletIndex}")
        }
        if (subscriptionsIndex.isNotEmpty()) {
            gemApiClient.addSubscriptions(deviceId, subscriptionsIndex.values.toList())
            configRepository.increaseSubscriptionVersion()
        }
    }

}

sealed class ImportError(message: String = "") : Exception(message) {

    data object InvalidationSecretPhrase : ImportError()

    class InvalidWords(val words: List<String>) : ImportError()

    data object InvalidAddress : ImportError()

    class CreateError(message: String) : ImportError(message)
}