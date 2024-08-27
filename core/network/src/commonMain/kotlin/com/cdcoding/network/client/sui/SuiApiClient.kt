package com.cdcoding.network.client.sui

import com.cdcoding.model.sui.SuiBroadcastTransaction
import com.cdcoding.model.sui.SuiTransaction
import com.cdcoding.network.model.JSONRpcRequest
import com.cdcoding.network.model.JSONRpcResponse
import com.cdcoding.network.util.NetworkError
import com.cdcoding.network.util.Result
import com.cdcoding.network.util.getResult
import com.cdcoding.model.sui.SuiData
import com.cdcoding.model.sui.SuiCoin
import com.cdcoding.model.sui.SuiCoinBalance
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.utils.EmptyContent.contentType
import io.ktor.http.ContentType
import io.ktor.http.contentType


class SuiApiClient(
        private val httpClient: HttpClient
    ) {

        private val SUI_URL = "https://sui.gemnodes.com"


        suspend fun coins(request: JSONRpcRequest<List<String>>): Result<JSONRpcResponse<SuiData<List<SuiCoin>>>, NetworkError> {
            return httpClient.post("$SUI_URL/") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }.getResult()
        }

        suspend fun balance(request: JSONRpcRequest<List<String>>): Result<JSONRpcResponse<SuiCoinBalance>, NetworkError> {
            return httpClient.post("$SUI_URL/") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }.getResult()
        }

        suspend fun balances(request: JSONRpcRequest<List<String>>): Result<JSONRpcResponse<List<SuiCoinBalance>>, NetworkError> {
            return httpClient.post("$SUI_URL/") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }.getResult()
        }

        suspend fun gasPrice(request: JSONRpcRequest<List<String>>): Result<JSONRpcResponse<String>, NetworkError> {
            return httpClient.post("$SUI_URL/") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }.getResult()
        }


        suspend fun dryRun(request: JSONRpcRequest<List<String>>): Result<JSONRpcResponse<SuiTransaction>, NetworkError> {
            return httpClient.post("$SUI_URL/") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }.getResult()
        }


        suspend fun transaction(request: JSONRpcRequest<List<Any>>): Result<JSONRpcResponse<SuiTransaction>, NetworkError> {
            return httpClient.post("$SUI_URL/") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }.getResult()
        }


        suspend fun broadcast(request: JSONRpcRequest<List<Any?>>): Result<JSONRpcResponse<SuiBroadcastTransaction>, NetworkError> {
            return httpClient.post("$SUI_URL/") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }.getResult()
        }

}

internal suspend fun SuiApiClient.coins(address: String, coinType: String): Result<JSONRpcResponse<SuiData<List<SuiCoin>>>, NetworkError> {
    return coins(JSONRpcRequest.create(SuiMethod.Coins, listOf(address, coinType)))
}

internal suspend fun SuiApiClient.balance(address: String): Result<JSONRpcResponse<SuiCoinBalance>, NetworkError> {
    return balance(JSONRpcRequest.create(SuiMethod.Balance, listOf(address)))
}

internal suspend fun SuiApiClient.balances(address: String): Result<JSONRpcResponse<List<SuiCoinBalance>>, NetworkError> {
    return balances(JSONRpcRequest.create(SuiMethod.Balances, listOf(address)))
}


internal suspend fun SuiApiClient.broadcast(data: String, sign: String): Result<JSONRpcResponse<SuiBroadcastTransaction>, NetworkError> {
    val request = JSONRpcRequest.create(
        SuiMethod.Broadcast,
        listOf(
            data,
            listOf(sign),
            null,
            "WaitForLocalExecution",
        )
    )
    return broadcast(request)
}


internal suspend fun SuiApiClient.transaction(txId: String): Result<JSONRpcResponse<SuiTransaction>, NetworkError> {
    val request = JSONRpcRequest.create(
        SuiMethod.Transaction,
        listOf(
            txId,
            mapOf("showEffects" to true)
        )
    )
    return transaction(request)
}
