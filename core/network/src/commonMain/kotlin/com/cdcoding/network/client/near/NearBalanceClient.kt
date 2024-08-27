package com.cdcoding.network.client.near

import com.cdcoding.model.AssetId
import com.cdcoding.model.Balances
import com.cdcoding.model.Chain
import com.cdcoding.network.client.BalanceClient
import com.cdcoding.network.model.JSONRpcRequest
import com.cdcoding.network.util.fold
import com.ionspin.kotlin.bignum.integer.BigInteger


class NearBalanceClient(
    private val chain: Chain,
    private val apiClient: NearApiClient,
) : BalanceClient {

    override suspend fun getNativeBalance(address: String): Balances? {
        return apiClient.account(
            JSONRpcRequest(
                method = NearMethod.Query.value,
                params = mapOf(
                    "request_type" to "view_account",
                    "finality" to "final",
                    "account_id" to address,
                )
            )
        ).fold(
            {
                if (it.error == null) {
                    Balances.create(AssetId(chain), BigInteger.parseString(it.result.amount))
                } else {
                    null
                }
            }
        ) {
            null
        }
    }

    override suspend fun getTokenBalances(address: String, tokens: List<AssetId>): List<Balances> {
        return emptyList()
    }

    override fun maintainChain(): Chain = chain
}