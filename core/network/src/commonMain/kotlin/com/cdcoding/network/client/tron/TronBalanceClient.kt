package com.cdcoding.network.client.tron

import com.cdcoding.model.AssetId
import com.cdcoding.model.Balances
import com.cdcoding.model.Chain
import com.cdcoding.model.tron.TronAccountRequest
import com.cdcoding.network.client.BalanceClient
import com.cdcoding.network.util.fold
import com.ionspin.kotlin.bignum.integer.BigInteger
import com.trustwallet.core.Base58


class TronBalanceClient(
    private val chain: Chain,
    private val apiClient: TronApiClient,
) : BalanceClient {

    override suspend fun getNativeBalance(address: String): Balances? {
        return apiClient.getAccount(TronAccountRequest(address, visible = true))
            .fold(
                {
                    Balances.create(AssetId(chain), BigInteger(it.balance ?: 0L))
                }
            ) {
                null
            }
    }

    @OptIn(ExperimentalStdlibApi::class)
    override suspend fun getTokenBalances(address: String, tokens: List<AssetId>): List<Balances> {
        return tokens.mapNotNull { assetId ->
            val tokenId = assetId.tokenId ?: return@mapNotNull null
            val owner = Base58.decode(address)?.toHexString() ?: ""
            apiClient.triggerSmartContract(
                contractAddress = Base58.decode(tokenId)?.toHexString() ?: "",
                functionSelector = "balanceOf(address)",
                parameter = owner.padStart(64, '0'),
                feeLimit = 1_000_000L,
                callValue = 0L,
                ownerAddress = owner,
                visible = false,
            ).fold(
                {
                    val amount = BigInteger.parseString(it.constant_result.firstOrNull() ?: "0", 16)
                    Balances.create(assetId, amount)
                }
            ) {
                null
            }
        }
    }

    override fun maintainChain(): Chain = Chain.Tron
}