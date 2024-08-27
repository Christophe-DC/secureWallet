package com.cdcoding.network.client.xrp

import com.cdcoding.common.utils.getReserveBalance
import com.cdcoding.model.AssetId
import com.cdcoding.model.Balances
import com.cdcoding.model.Chain
import com.cdcoding.network.client.BalanceClient
import com.cdcoding.network.util.getOrNull
import com.cdcoding.network.util.map
import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.toBigInteger


class XrpBalanceClient(
    private val chain: Chain,
    private val apiClient: XrpApiClient,
) : BalanceClient {
    private val reservedBalance = BigInteger(10_000_000)
    override fun maintainChain(): Chain = chain

    override suspend fun getNativeBalance(address: String): Balances {
        val amount = apiClient.account(address).map {
            it.result.account_data.Balance.toBigInteger() - chain.getReserveBalance()
        }.getOrNull() ?: BigInteger.ZERO
        return Balances.create(AssetId(chain), amount, reserved = reservedBalance)
    }

    override suspend fun getTokenBalances(address: String, tokens: List<AssetId>): List<Balances> = emptyList()

}