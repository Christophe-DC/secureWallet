package com.cdcoding.network.client

import com.cdcoding.common.utils.type
import com.cdcoding.model.Account
import com.cdcoding.model.AssetId
import com.cdcoding.model.AssetSubtype
import com.cdcoding.model.Balances


interface BalancesRemoteSource {
    suspend fun getBalances(account: Account, tokens: List<AssetId>): Result<List<Balances>>
}

class BalancesRetrofitRemoteSource (
    private val balanceClients: List<BalanceClient>,
) : BalancesRemoteSource {

    override suspend fun getBalances(account: Account, tokens: List<AssetId>): Result<List<Balances>> {
        val client = balanceClients.firstOrNull { it.isMaintain(account.chain) }
            ?: return Result.failure(Exception("Chain doesn't support"))
        val nativeBalances = try {
            client.getNativeBalance(account.address)
        } catch (err: Throwable) {
            null
        }

        val tokensBalances = if (tokens.isEmpty()) {
            emptyList()
        } else {
            try {
                client.getTokenBalances(
                    account.address,
                    tokens.filter { it.type() == AssetSubtype.TOKEN && account.chain == it.chain },
                )
            } catch (err: Throwable) {
                emptyList()
            }
        }
        val result = if (nativeBalances == null) {
            tokensBalances
        } else {
            tokensBalances + nativeBalances
        }

        return Result.success(result)
    }
}