package com.cdcoding.network.client.tron

import com.cdcoding.model.tron.TronAccount
import com.cdcoding.model.tron.TronAccountRequest
import com.cdcoding.model.tron.TronAccountUsage
import com.cdcoding.model.tron.TronBlock
import com.cdcoding.model.tron.TronSmartContractResult
import com.cdcoding.model.tron.TronTransactionBroadcast
import com.cdcoding.network.util.NetworkError
import io.ktor.client.HttpClient
import com.cdcoding.network.util.Result
import com.cdcoding.network.util.getResult
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType


class TronApiClient(
    private val httpClient: HttpClient
) {

    private val TRON_URL = "https://tron.gemnodes.com"


    suspend fun nowBlock(): Result<TronBlock, NetworkError> {
        return httpClient.post("$TRON_URL/wallet/getnowblock").getResult()
    }

    suspend fun getChainParameters(): Result<TronChainParameters, NetworkError> {
        return httpClient.get("$TRON_URL/wallet/getchainparameters").getResult()
    }

    suspend fun getAccount(addressRequest: TronAccountRequest): Result<TronAccount, NetworkError> {
        return httpClient.post("$TRON_URL/wallet/getaccount") {
            contentType(ContentType.Application.Json)
            setBody(addressRequest)
        }.getResult()
    }

    suspend fun getAccountUsage(addressRequest: TronAccountRequest): Result<TronAccountUsage, NetworkError> {
        return httpClient.post("$TRON_URL/wallet/getaccountnet") {
            contentType(ContentType.Application.Json)
            setBody(addressRequest)
        }.getResult()
    }

    suspend fun triggerSmartContract(addressRequest: Any): Result<TronSmartContractResult, NetworkError> {
        return httpClient.post("$TRON_URL/wallet/triggerconstantcontract") {
            contentType(ContentType.Application.Json)
            setBody(addressRequest)
        }.getResult()
    }

    suspend fun broadcast(body: ByteArray): Result<TronTransactionBroadcast, NetworkError> {
        return httpClient.post("$TRON_URL/wallet/broadcasttransaction") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.getResult()
    }


    class TronValue(val value: String)
}

suspend fun TronApiClient.triggerSmartContract(
    contractAddress: String,
    functionSelector: String,
    parameter: String? = null,
    feeLimit: Long? = null,
    callValue: Long? = null,
    ownerAddress: String,
    visible: Boolean? = null
): Result<TronSmartContractResult, NetworkError> {
    val call = mapOf(
        "contract_address" to contractAddress,
        "function_selector" to functionSelector,
        "parameter" to parameter,
        "fee_limit" to feeLimit,
        "call_value" to callValue,
        "owner_address" to ownerAddress,
        "visible" to visible,
    )
    return triggerSmartContract(call)
}
