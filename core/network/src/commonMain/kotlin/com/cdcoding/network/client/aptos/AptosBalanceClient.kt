package com.cdcoding.network.client.aptos

import com.cdcoding.model.AssetId
import com.cdcoding.model.Balances
import com.cdcoding.model.Chain
import com.cdcoding.network.client.BalanceClient
import com.cdcoding.network.util.fold
import com.ionspin.kotlin.bignum.integer.toBigInteger


class AptosBalanceClient(
    private val chain: Chain,
    private val apiClient: AptosApiClient,
) : BalanceClient {
    override suspend fun getNativeBalance(address: String): Balances? {
        return apiClient.balance(address)
            .fold({ Balances.create(AssetId(chain), it.data.coin.value.toBigInteger()) }) { null }
    }

    override suspend fun getTokenBalances(address: String, tokens: List<AssetId>): List<Balances> = emptyList()

    override fun maintainChain(): Chain = chain
}