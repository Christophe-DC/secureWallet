package com.cdcoding.network.client.ethereum

import com.cdcoding.network.model.BitcoinAccount
import com.cdcoding.network.util.NetworkError
import com.cdcoding.network.util.getResult
import io.ktor.client.HttpClient
import io.ktor.client.request.get


class EvmRpcClient (
    private val httpClient: HttpClient
) {

    private val ETHEREUM_URL = "https://ethereum.gemnodes.com"

    suspend fun getBalance(address: String): com.cdcoding.network.util.Result<BitcoinAccount, NetworkError> {
        return httpClient.get("$ETHEREUM_URL/api/v2/address/$address").getResult()
    }
}