package com.cdcoding.network.client.ethereum

import com.cdcoding.common.utils.decodeHex
import com.cdcoding.model.AssetId
import com.cdcoding.model.Balances
import com.cdcoding.model.Chain
import com.cdcoding.network.client.BalanceClient
import com.cdcoding.network.model.JSONRpcRequest
import com.cdcoding.network.util.fold
import com.cdcoding.network.util.getOrNull
import com.trustwallet.core.EthereumAbi
import com.trustwallet.core.EthereumAbiFunction


class EvmBalanceClient(
    private val chain: Chain,
    private val apiClient: EvmApiClient,
) : BalanceClient {

    override suspend fun getNativeBalance(address: String): Balances? {
        return apiClient.getBalance(address)
            .fold({ Balances.create(AssetId(chain), it.result?.value ?: return null) }) { null }
    }

    override suspend fun getTokenBalances(address: String, tokens: List<AssetId>): List<Balances> {
        if (tokens.isEmpty()) {
            return emptyList()
        }
        val result = mutableListOf<Balances>()
        for (token in tokens) {
            val data = "0x70a08231000000000000000000000000${address.removePrefix("0x")}"
            val contract = token.tokenId ?: continue
            val params = mapOf(
                "to" to contract,
                "data" to data,
            )
            val balance = apiClient.callNumber(
                JSONRpcRequest.create(
                    EvmMethod.Call,
                    listOf(params, "latest")
                )
            )
                .getOrNull()?.result?.value ?: continue
            result.add(Balances.create(token, available = balance))
        }
        return result
    }

    override fun maintainChain(): Chain = chain

    @OptIn(ExperimentalStdlibApi::class)
    private fun encodeBalanceOf(address: String): String {
        val function = EthereumAbiFunction("balanceOf")
        val addressData = address.decodeHex()
        function.addParamAddress(addressData, false)
        function.addParamUInt256(byteArrayOf(), true)
        return EthereumAbi.encode(function).toHexString()
    }
}