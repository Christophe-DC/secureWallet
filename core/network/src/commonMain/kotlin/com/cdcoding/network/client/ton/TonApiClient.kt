package com.cdcoding.network.client.ton

import com.cdcoding.model.aptos.AptosAccount
import com.cdcoding.model.sui.SuiTransaction
import com.cdcoding.model.ton.TonBroadcastTransaction
import com.cdcoding.model.ton.TonJettonBalance
import com.cdcoding.model.ton.TonResult
import com.cdcoding.model.ton.TonWalletInfo
import com.cdcoding.network.model.JSONRpcRequest
import com.cdcoding.network.util.NetworkError
import com.cdcoding.network.util.getResult
import com.cdcoding.network.util.Result
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class TonApiClient(
    private val httpClient: HttpClient
) {

    private val TON_URL = "https://ton.gemnodes.com"

    suspend fun balance(address: String): Result<TonResult<String>, NetworkError> {
        return httpClient.get("$TON_URL/api/v2/getAddressBalance") {
            parameter("address", address)
        }
            .getResult()
    }

    suspend fun addressState(address: String): Result<TonResult<String>, NetworkError> {
        return httpClient.get("$TON_URL/api/v2/getAddressState") {
            parameter("address", address)
        }
            .getResult()
    }

    suspend fun walletInfo(address: String): Result<TonResult<TonWalletInfo>, NetworkError> {
        return httpClient.get("$TON_URL/api/v2/getWalletInformation") {
            parameter("address", address)
        }
            .getResult()
    }

    suspend fun getJetonAddress(request: JSONRpcRequest<Any>): Result<JetonAddress, NetworkError> {
        return httpClient.post("$TON_URL/api/v2/jsonRPC") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.getResult()
    }

    suspend fun broadcast(boc: Boc): Result<TonResult<TonBroadcastTransaction>, NetworkError> {
        return httpClient.post("$TON_URL/api/v2/sendBocReturnHash") {
            contentType(ContentType.Application.Json)
            setBody(boc)
        }.getResult()
    }


    suspend fun tokenBalance(address: String): Result<TonResult<TonJettonBalance>, NetworkError> {
        return httpClient.get("$TON_URL/api/v2/getTokenData") {
            parameter("address", address)
        }
            .getResult()
    }

    data class Boc(
        val boc: String
    )


    data class JetonAddress(
        val b64: String,
        val len: Long,
    )
}