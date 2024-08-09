package com.cdcoding.domain

import com.cdcoding.common.utils.ChainInfoLocalSource
import com.cdcoding.common.utils.getAccount
import com.cdcoding.common.utils.toIdentifier
import com.cdcoding.data.repository.AssetRepository
import com.cdcoding.data.repository.TokenRepository
import com.cdcoding.model.AssetInfo
import com.cdcoding.model.Wallet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetAssetsByQueryUseCase(
    private val assetRepository: AssetRepository,
    private val tokenRepository: TokenRepository,
) {

    suspend operator fun invoke(wallet: Wallet, query: String): Flow<List<AssetInfo>> {
        val assetsFlow = assetRepository.getAllByAccounts(wallet.accounts, query)
        val tokensFlow = tokenRepository.search(wallet.accounts.map { it.chain }, query)
        return combine(
            assetsFlow,
            tokensFlow,
        ) { assets, tokens ->
            val result = assets + tokens.mapNotNull { asset ->
                AssetInfo(
                    asset = asset,
                    owner = wallet.getAccount(asset.id.chain) ?: return@mapNotNull null,
                )
            }.filter { !ChainInfoLocalSource.exclude.contains(it.asset.id.chain) }
            result.distinctBy {
                it.asset.id.toIdentifier()
            }
            
        }
    }
}