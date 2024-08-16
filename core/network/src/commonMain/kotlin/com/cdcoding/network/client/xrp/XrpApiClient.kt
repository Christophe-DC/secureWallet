package com.cdcoding.network.client.xrp

import com.cdcoding.model.xrp.XRPAccountResult
import com.cdcoding.model.xrp.XRPFee
import com.cdcoding.model.xrp.XRPResult
import com.cdcoding.model.xrp.XRPTransactionBroadcast
import com.cdcoding.model.xrp.XRPTransactionStatus
import com.cdcoding.network.model.JSONRpcRequest
import com.cdcoding.network.util.NetworkError
import com.cdcoding.network.util.getResult
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import com.cdcoding.network.util.Result

class XrpApiClient(
    private val httpClient: HttpClient
) {

    private val XRP_URL = "https://xrp.gemnodes.com"


    suspend fun account(request: JSONRpcRequest<List<Map<String, String>>>): Result<XRPResult<XRPAccountResult>, NetworkError> {
        return httpClient.post("$XRP_URL/") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.getResult()
    }

    suspend fun fee(request: JSONRpcRequest<List<Map<String, String>>>): Result<XRPResult<XRPFee>, NetworkError> {
        return httpClient.post("$XRP_URL/") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.getResult()
    }

    suspend fun transaction(request: JSONRpcRequest<List<Map<String, String>>>): Result<XRPResult<XRPTransactionStatus>, NetworkError> {
        return httpClient.post("$XRP_URL/") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.getResult()
    }

    suspend fun broadcast(request: JSONRpcRequest<List<Map<String, String>>>): Result<XRPResult<XRPTransactionBroadcast>, NetworkError> {
        return httpClient.post("$XRP_URL/") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.getResult()
    }

}

internal suspend fun XrpApiClient.account(address: String): Result<XRPResult<XRPAccountResult>, NetworkError> {
    val request = JSONRpcRequest.create(
        XrpMethod.Account,
        listOf(
            mapOf(
                "account" to address,
                "ledger_index" to "current",
            )
        )
    )
    return account(request)
}

internal suspend fun XrpApiClient.fee(): Result<XRPResult<XRPFee>, NetworkError> {
    return fee(JSONRpcRequest.create(XrpMethod.Fee, listOf(mapOf())))
}

internal suspend fun XrpApiClient.transaction(txId: String): Result<XRPResult<XRPTransactionStatus>, NetworkError> {
    val request = JSONRpcRequest.create(
        XrpMethod.Transaction,
        listOf(
            mapOf(
                "transaction" to txId,
            )
        )
    )
    return transaction(request)
}

internal suspend fun XrpApiClient.broadcast(data: String): Result<XRPResult<XRPTransactionBroadcast>, NetworkError> {
    val request = JSONRpcRequest.create(
        XrpMethod.Broadcast,
        listOf(
            mapOf(
                "tx_blob" to data,
                "fail_hard" to "true"
            )
        )
    )
    return broadcast(request)
}