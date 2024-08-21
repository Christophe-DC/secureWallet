package com.cdcoding.network.client.cosmo

import com.cdcoding.model.AssetId
import com.cdcoding.model.Balances
import com.cdcoding.model.Chain
import com.cdcoding.model.cosmos.CosmosDenom
import com.cdcoding.network.client.BalanceClient
import com.cdcoding.network.util.getOrNull
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import com.ionspin.kotlin.bignum.integer.BigInteger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class CosmosBalanceClient(
    private val chain: Chain,
    private val apiClient: CosmosApiClient,
) : BalanceClient {

    override suspend fun getNativeBalance(address: String): Balances =
        withContext(Dispatchers.IO) {
            val assetId = AssetId(chain)
            val denom = CosmosDenom.from(chain)

            val getBalances = async { apiClient.getBalance(address).getOrNull()?.balances }
            val balance = getBalances.await()
                ?.filter { it.denom == denom }
                ?.map { it.amount.toBigDecimal().toBigInteger() }
                ?.reduceOrNull { acc, value -> acc + value } ?: BigInteger.ZERO

            return@withContext Balances.create(assetId, balance)

        }

    override suspend fun getTokenBalances(address: String, tokens: List<AssetId>): List<Balances> {
        val balances = try {
            apiClient.getBalance(address).getOrNull() ?: return emptyList()
        } catch (err: Throwable) {
            return emptyList()
        }
        return tokens.map { assetId ->
            val amount =
                balances.balances.firstOrNull { it.denom == assetId.tokenId }?.amount ?: "0"
            Balances.create(
                assetId,
                available = try {
                    BigInteger.parseString(amount)
                } catch (err: Throwable) {
                    return@map null
                }
            )
        }.mapNotNull { it }
    }

    override fun maintainChain(): Chain = chain
}

fun CosmosDenom.Companion.from(chain: Chain): String = when (chain) {
    Chain.Cosmos -> CosmosDenom.Uatom.string
    Chain.Osmosis -> CosmosDenom.Uosmo.string
    Chain.Thorchain -> CosmosDenom.Rune.string
    Chain.Celestia -> CosmosDenom.Utia.string
    Chain.Injective -> CosmosDenom.Inj.string
    Chain.Sei -> CosmosDenom.Usei.string
    Chain.Noble -> CosmosDenom.Uusdc.string
    else -> throw IllegalArgumentException("Coin is not supported")
}