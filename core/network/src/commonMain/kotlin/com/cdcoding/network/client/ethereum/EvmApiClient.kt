package com.cdcoding.network.client.ethereum

import com.cdcoding.model.ethereum.EthereumFeeHistory
import com.cdcoding.network.model.JSONRpcRequest
import com.cdcoding.network.model.JSONRpcResponse
import com.cdcoding.network.util.NetworkError
import com.cdcoding.network.util.Result
import com.cdcoding.network.util.getOrNull
import com.cdcoding.network.util.getResult
import com.ionspin.kotlin.bignum.integer.BigInteger
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType


class EvmApiClient (
    private val httpClient: HttpClient
) {

    private val ETHEREUM_URL = "https://ethereum.gemnodes.com"

    suspend fun getBalance(request: JSONRpcRequest<List<String>>): Result<JSONRpcResponse<EvmNumber?>, NetworkError> {
        return httpClient.post("$ETHEREUM_URL/"){
            contentType(ContentType.Application.Json)
            setBody(request)
        }.getResult()
    }

    suspend fun getNetVersion(request: JSONRpcRequest<List<String>>): Result<JSONRpcResponse<EvmNumber?>, NetworkError> {
        return httpClient.post("$ETHEREUM_URL/"){
            contentType(ContentType.Application.Json)
            setBody(request)
        }.getResult()
    }


    suspend fun getGasLimit(request: JSONRpcRequest<List<Transaction>>): Result<JSONRpcResponse<EvmNumber?>, NetworkError> {
        return httpClient.post("$ETHEREUM_URL/"){
            contentType(ContentType.Application.Json)
            setBody(request)
        }.getResult()
    }

    suspend fun getNonce(request: JSONRpcRequest<List<String>>): Result<JSONRpcResponse<EvmNumber?>, NetworkError> {
        return httpClient.post("$ETHEREUM_URL/"){
            contentType(ContentType.Application.Json)
            setBody(request)
        }.getResult()
    }


    suspend fun broadcast(request: JSONRpcRequest<List<String>>): Result<JSONRpcResponse<String>, NetworkError> {
        return httpClient.post("$ETHEREUM_URL/"){
            contentType(ContentType.Application.Json)
            setBody(request)
        }.getResult()
    }

    suspend fun callNumber(request: JSONRpcRequest<List<Any>>): Result<JSONRpcResponse<EvmNumber?>, NetworkError> {
        return httpClient.post("$ETHEREUM_URL/"){
            contentType(ContentType.Application.Json)
            setBody(request)
        }.getResult()
    }


    suspend fun callString(request: JSONRpcRequest<List<Any>>): Result<JSONRpcResponse<String?>, NetworkError> {
        return httpClient.post("$ETHEREUM_URL/"){
            contentType(ContentType.Application.Json)
            setBody(request)
        }.getResult()
    }

    suspend fun getFeeHistory(request: JSONRpcRequest<List<Any>>): Result<JSONRpcResponse<EthereumFeeHistory>, NetworkError> {
        return httpClient.post("$ETHEREUM_URL/"){
            contentType(ContentType.Application.Json)
            setBody(request)
        }.getResult()
    }


    class EvmNumber(
        val value: BigInteger?,
    )

    class Transaction(
        val from: String,
        val to: String,
        val value: String?,
        val data: String?,
    )
}



internal suspend fun EvmApiClient.getBalance(address: String): Result<JSONRpcResponse<EvmApiClient.EvmNumber?>, NetworkError> {
    return getBalance(
        JSONRpcRequest.create(
            method = EvmMethod.GetBalance,
            params = listOf(address, "latest")
        )
    )
}


internal suspend fun EvmApiClient.callString(contract: String, hexData: String): String? {
    val params = mapOf(
        "to" to contract,
        "data" to hexData
    )
    val request = JSONRpcRequest.create(
        EvmMethod.Call,
        listOf(
            params,
            "latest"
        )
    )
    return callString(request).getOrNull()?.result
}