package com.cdcoding.domain

import com.cdcoding.common.utils.getSwapMetadata
import com.cdcoding.data.repository.AssetRepository
import com.cdcoding.data.repository.TokenRepository
import com.cdcoding.data.repository.TransactionRepository
import com.cdcoding.datastore.ConfigRepository
import com.cdcoding.model.AssetId
import com.cdcoding.model.Transaction
import com.cdcoding.network.client.GemApiClient
import com.cdcoding.network.util.getOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class SyncTransactionUseCase(
    private val gemApiClient: GemApiClient,
    private val configRepository: ConfigRepository,
    private val transactionRepository: TransactionRepository,
    private val assetRepository: AssetRepository,
    private val tokensRepository: TokenRepository,
) {

    suspend operator fun invoke(walletIndex: Int) = withContext(Dispatchers.IO) {
        val deviceId = configRepository.getDeviceId()
        val lastSyncTime = configRepository.getTxSyncTime()
        val txs = gemApiClient.getTransactions(deviceId, walletIndex, lastSyncTime).getOrNull() ?: return@withContext
        prefetchAssets(txs)

        transactionRepository.putTransactions(txs.toList())

        configRepository.setTxSyncTime(txs.map { listOf(it.createdAt) }.flatten().maxByOrNull { it } ?: 0L)
    }


    private suspend fun prefetchAssets(txs: List<Transaction>) {
        txs.map {
            val notAvailableAssetsId = mutableListOf<AssetId>()
            if (assetRepository.getAsset(it.assetId) == null) {
                notAvailableAssetsId.add(it.assetId)
            }
            if (assetRepository.getAsset(it.feeAssetId) == null) {
                notAvailableAssetsId.add(it.assetId)
            }
            val swapMetadata = it.getSwapMetadata()
            if (swapMetadata != null) {
                if (assetRepository.getAsset(swapMetadata.fromAsset) == null) {
                    notAvailableAssetsId.add(swapMetadata.fromAsset)
                }
                if (assetRepository.getAsset(swapMetadata.toAsset) == null) {
                    notAvailableAssetsId.add(swapMetadata.toAsset)
                }
            }
            notAvailableAssetsId.toSet()
        }.flatten().forEach {  assetId ->
            tokensRepository.search(assetId)
        }
    }
}