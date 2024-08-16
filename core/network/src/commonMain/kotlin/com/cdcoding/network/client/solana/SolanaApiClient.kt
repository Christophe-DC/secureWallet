package com.cdcoding.network.client.solana

import com.cdcoding.model.solana.SolanaBlockhashResult
import com.cdcoding.model.solana.SolanaPrioritizationFee
import com.cdcoding.model.solana.SolanaStakeAccount
import com.cdcoding.model.solana.SolanaTokenAccount
import com.cdcoding.model.solana.SolanaTokenAccountResult
import com.cdcoding.model.solana.SolanaValue
import com.cdcoding.network.model.JSONRpcRequest
import com.cdcoding.network.model.JSONRpcResponse
import com.cdcoding.network.util.NetworkError
import com.cdcoding.network.util.getResult
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import com.cdcoding.network.util.Result

class SolanaApiClient(
    private val httpClient: HttpClient
) {

    private val SOLANA_URL = "https://solana.gemnodes.com"


    suspend fun getTokenAccountByOwner(request: JSONRpcRequest<List<Any>>): Result<JSONRpcResponse<SolanaValue<List<SolanaTokenAccount>>>, NetworkError> {
        return httpClient.post("$SOLANA_URL/") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.getResult()
    }

    suspend fun getPriorityFees(request: JSONRpcRequest<List<String>>): Result<JSONRpcResponse<List<SolanaPrioritizationFee>>, NetworkError> {
        return httpClient.post("$SOLANA_URL/") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.getResult()
    }

    suspend fun broadcast(request: JSONRpcRequest<List<Any>>): Result<JSONRpcResponse<String>, NetworkError> {
        return httpClient.post("$SOLANA_URL/") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.getResult()
    }

    suspend fun rentExemption(request: JSONRpcRequest<List<Int>>): Result<JSONRpcResponse<Int>, NetworkError> {
        return httpClient.post("$SOLANA_URL/") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.getResult()
    }


    suspend fun getBlockhash(request: JSONRpcRequest<List<String>>): Result<JSONRpcResponse<SolanaBlockhashResult>, NetworkError> {
        return httpClient.post("$SOLANA_URL/") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.getResult()
    }


    suspend fun delegations(request: JSONRpcRequest<List<Any>>): Result<JSONRpcResponse<List<SolanaTokenAccountResult<SolanaStakeAccount>>>, NetworkError> {
        return httpClient.post("$SOLANA_URL/") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.getResult()
    }


}

suspend fun SolanaApiClient.getTokenAccountByOwner(
    owner: String,
    tokenId: String,
): Result<JSONRpcResponse<SolanaValue<List<SolanaTokenAccount>>>, NetworkError> {
    val accountRequest = JSONRpcRequest.create(
        method = SolanaMethod.GetTokenAccountByOwner,
        params = listOf(
            owner,
            mapOf("mint" to tokenId),
            mapOf("encoding" to "jsonParsed"),
        )
    )
    return getTokenAccountByOwner(accountRequest)
}

suspend fun SolanaApiClient.delegations(
    owner: String,
): Result<JSONRpcResponse<List<SolanaTokenAccountResult<SolanaStakeAccount>>>, NetworkError> {
    val request = JSONRpcRequest.create(
        SolanaMethod.GetDelegations,
        listOf(
            "Stake11111111111111111111111111111111111111",
            mapOf(
                "encoding" to "jsonParsed",
                "commitment" to "finalized",
                "filters" to listOf(
                    mapOf(
                        "memcmp" to mapOf(
                            "bytes" to owner,
                            "offset" to 44,
                        )
                    )
                )
            )
        )
    )
    return delegations(request)
}