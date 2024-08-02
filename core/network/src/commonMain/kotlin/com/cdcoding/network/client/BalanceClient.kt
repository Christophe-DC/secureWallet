package com.cdcoding.network.client

import com.cdcoding.model.AssetId
import com.cdcoding.model.Balances


interface BalanceClient : BlockchainClient {
    suspend fun getNativeBalance(address: String): Balances?

    suspend fun getTokenBalances(address: String, tokens: List<AssetId>): List<Balances>
}