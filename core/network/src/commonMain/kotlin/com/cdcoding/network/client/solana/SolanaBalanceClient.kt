package com.cdcoding.network.client.solana

import com.cdcoding.model.AssetId
import com.cdcoding.model.Balances
import com.cdcoding.model.Chain
import com.cdcoding.network.client.BalanceClient
import com.cdcoding.network.model.JSONRpcRequest
import com.cdcoding.network.util.getOrNull
import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.toBigInteger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class SolanaBalanceClient(
    val chain: Chain,
    val apiClient: SolanaApiClient,
) : BalanceClient {

    override suspend fun getNativeBalance(address: String): Balances? = withContext(Dispatchers.IO) {
        val getAvailable = async {
            apiClient.getBalance(JSONRpcRequest.create(SolanaMethod.GetBalance, listOf(address)))
                .getOrNull()?.result?.value
        }
        val getStaked = async {
            apiClient.delegations(address)
                .getOrNull()?.result?.map { it.account.lamports }
                ?.fold(0L) { acc, value -> acc + value } ?: 0L
        }
        val (available, staked) = Pair(getAvailable.await(), getStaked.await())
        if (available == null) {
            return@withContext null
        }
        Balances.create(
            assetId = AssetId(chain),
            available = available.toBigInteger(),
            staked = staked.toBigInteger(),
        )
    }

    override suspend fun getTokenBalances(address: String, tokens: List<AssetId>): List<Balances> {
        val result = mutableListOf<Balances>()
        for (assetId in tokens) {
            val tokenId = assetId.tokenId ?: continue
            val balance = getTokenBalance(address, tokenId)
            result.add(Balances.create(assetId, balance))
        }
        return result
    }

    private suspend fun getTokenBalance(owner: String, tokenId: String): BigInteger {
        val accountRequest = JSONRpcRequest.create(
            method = SolanaMethod.GetTokenAccountByOwner,
            params = listOf(
                owner,
                mapOf("mint" to tokenId),
                mapOf("encoding" to "jsonParsed"),
            )
        )
        val tokenAccount = apiClient.getTokenAccountByOwner(accountRequest)
            .getOrNull()?.result?.value?.firstOrNull()?.pubkey ?: return BigInteger.ZERO

        val balanceRequest = JSONRpcRequest.create(SolanaMethod.GetTokenBalance, listOf(tokenAccount))
        return apiClient.getTokenBalance(balanceRequest).getOrNull()
            ?.result?.value?.amount?.toBigInteger() ?: return BigInteger.ZERO
    }

    override fun maintainChain(): Chain = Chain.Solana
}