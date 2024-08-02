package com.cdcoding.domain

import com.cdcoding.data.repository.AssetRepository
import com.cdcoding.model.AssetInfo
import com.cdcoding.model.Wallet
import kotlinx.coroutines.flow.Flow

class GetAssetsByWalletUseCase(
    private val assetRepository: AssetRepository,
) {

    operator fun invoke(wallet: Wallet): Flow<List<AssetInfo>> = assetRepository.getAllByWalletFlow(wallet)
}