package com.cdcoding.network.client.sui

import com.cdcoding.model.AssetId
import com.cdcoding.model.Balances
import com.cdcoding.model.Chain
import com.cdcoding.network.client.BalanceClient
import com.cdcoding.network.util.getOrNull
import com.cdcoding.network.util.map
import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.toBigInteger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class SuiBalanceClient(
    private val chain: Chain,
    private val apiClient: SuiApiClient,
) : BalanceClient {
    override fun maintainChain(): Chain = chain

    override suspend fun getNativeBalance(address: String): Balances = withContext(Dispatchers.IO) {
        val amount = apiClient.balance(address).map {
                it.result.totalBalance.toBigInteger()
            }.getOrNull() ?: BigInteger.ZERO
        Balances.create(AssetId(chain), amount)
    }

    override suspend fun getTokenBalances(address: String, tokens: List<AssetId>): List<Balances> {
        return apiClient.balances(address).map { response ->
            val balances = response.result
            tokens.mapNotNull { assetId ->
                Balances.create(
                    assetId,
                    balances.firstOrNull{ assetId.tokenId == it.coinType }?.totalBalance?.toBigInteger() ?: return@mapNotNull null,
                )
            }
        }.getOrNull() ?: emptyList()
    }
}