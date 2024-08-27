package com.cdcoding.network.client.bitcoin

import com.cdcoding.model.AssetId
import com.cdcoding.model.Balances
import com.cdcoding.model.Chain
import com.cdcoding.network.client.BalanceClient
import com.cdcoding.network.util.fold
import com.ionspin.kotlin.bignum.integer.toBigInteger


class BitcoinBalanceClient(
    private val chain: Chain,
    private val client: BitcoinApiClient,
) : BalanceClient {

    override suspend fun getNativeBalance(address: String): Balances? {

        return client.getBalance(address).fold (
            {
                if (it.balance.isNotEmpty()) {
                    Balances.create(AssetId(chain), it.balance.toBigInteger())
                } else {
                    null
                }
            }
        ){
            null
        }
    }

    override suspend fun getTokenBalances(address: String, tokens: List<AssetId>): List<Balances> = emptyList()

    override fun maintainChain(): Chain = chain
}