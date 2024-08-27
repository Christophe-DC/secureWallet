package com.cdcoding.network.client.ton


import com.cdcoding.model.AssetId
import com.cdcoding.model.Balances
import com.cdcoding.model.Chain
import com.cdcoding.network.client.BalanceClient
import com.cdcoding.network.util.fold
import com.cdcoding.network.util.getOrNull
import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.toBigInteger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.toList

class TonBalanceClient(
    private val chain: Chain,
    private val apiClient: TonApiClient,
) : BalanceClient {

    override suspend fun getNativeBalance(address: String): Balances? {
        return apiClient.balance(address)
            .fold( { Balances.create(AssetId(chain), it.result.toBigInteger()) } ) { null }
    }

    override suspend fun getTokenBalances(address: String, tokens: List<AssetId>): List<Balances> {
        return tokens.asFlow()
            .mapNotNull {
                val tokenId = it.tokenId ?: return@mapNotNull null
                val jettonAddress = jettonAddress(apiClient, tokenId, address) ?: return@mapNotNull null
                val isActive = apiClient.addressState(jettonAddress).getOrNull()?.result == "active"

                if (isActive) {
                    Balances.create(it, tokenBalance(jettonAddress))
                } else {
                    Balances.create(it, BigInteger.ZERO)
                }
            }
            .flowOn(Dispatchers.IO)
            .toList()
    }

    override fun maintainChain(): Chain = Chain.Ton

    private suspend fun tokenBalance(jettonAddress: String): BigInteger {
        return BigInteger(
            apiClient.tokenBalance(jettonAddress).getOrNull()?.result?.balance ?: 0L
        )
    }
}