package com.cdcoding.network.client.aptos

import com.cdcoding.model.aptos.AptosAccount
import com.cdcoding.model.aptos.AptosGasFee
import com.cdcoding.model.aptos.AptosResource
import com.cdcoding.model.aptos.AptosResourceBalance
import com.cdcoding.model.aptos.AptosTransactionBroacast
import com.cdcoding.model.bitcoin.BitcoinTransactionBroacastResult
import com.cdcoding.network.util.NetworkError
import com.cdcoding.network.util.getResult
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import com.cdcoding.network.util.Result
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AptosApiClient(
    private val httpClient: HttpClient
) {

    private val APTOS_URL = "https://aptos.gemnodes.com"

    suspend fun getAccounts(address: String): Result<AptosAccount, NetworkError> {
        return httpClient.get("$APTOS_URL/v1/accounts/$address").getResult()
    }

    suspend fun balance(address: String): Result<AptosResource<AptosResourceBalance>, NetworkError> {
        return httpClient.get("$APTOS_URL/v1/accounts/$address/resource/0x1::coin::CoinStore<0x1::aptos_coin::AptosCoin>").getResult()
    }

    suspend fun getFeePrice(): Result<AptosGasFee, NetworkError> {
        return httpClient.get("$APTOS_URL/v1/estimate_gas_price").getResult()
    }


    suspend fun broadcast(request: ByteArray): Result<AptosTransactionBroacast, NetworkError> {
        return httpClient.post("$APTOS_URL/v1/transactions"){
            contentType(ContentType.Application.Json)
            setBody(request)
        }.getResult()
    }
}