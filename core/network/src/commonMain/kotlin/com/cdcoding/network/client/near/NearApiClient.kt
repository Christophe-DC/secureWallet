package com.cdcoding.network.client.near

import com.cdcoding.model.near.NearAccount
import com.cdcoding.model.near.NearAccountAccessKey
import com.cdcoding.model.near.NearBlock
import com.cdcoding.model.near.NearBroadcastResult
import com.cdcoding.model.near.NearGasPrice
import com.cdcoding.network.model.JSONRpcRequest
import com.cdcoding.network.model.JSONRpcResponse
import com.cdcoding.network.util.NetworkError
import com.cdcoding.network.util.Result
import com.cdcoding.network.util.getResult
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType


class NearApiClient(
    private val httpClient: HttpClient
) {

    private val NEAR_URL = "https://near.gemnodes.com"

    suspend fun account(params: JSONRpcRequest<Any>): Result<JSONRpcResponse<NearAccount>, NetworkError> {
        return httpClient.post("$NEAR_URL/") {
            contentType(ContentType.Application.Json)
            setBody(params)
        }.getResult()
    }

    suspend fun accountAccessKey(params: JSONRpcRequest<Any>): Result<JSONRpcResponse<NearAccountAccessKey>, NetworkError> {
        return httpClient.post("$NEAR_URL/") {
            contentType(ContentType.Application.Json)
            setBody(params)
        }.getResult()
    }

    suspend fun latestBlock(params: JSONRpcRequest<Any>): Result<JSONRpcResponse<NearBlock>, NetworkError> {
        return httpClient.post("$NEAR_URL/"){
            contentType(ContentType.Application.Json)
            setBody(params)
        }.getResult()
    }

    suspend fun getGasPrice(params: JSONRpcRequest<List<String?>>): Result<JSONRpcResponse<NearGasPrice>, NetworkError> {
        return httpClient.post("$NEAR_URL/"){
            contentType(ContentType.Application.Json)
            setBody(params)
        }.getResult()
    }


    suspend fun transaction(params: JSONRpcRequest<Any>): Result<JSONRpcResponse<NearBroadcastResult>, NetworkError> {
        return httpClient.post("$NEAR_URL/"){
            contentType(ContentType.Application.Json)
            setBody(params)
        }.getResult()
    }

    suspend fun broadcast(params: JSONRpcRequest<Any>): Result<JSONRpcResponse<NearBroadcastResult>, NetworkError> {
        return httpClient.post("$NEAR_URL/"){
            contentType(ContentType.Application.Json)
            setBody(params)
        }.getResult()
    }


}