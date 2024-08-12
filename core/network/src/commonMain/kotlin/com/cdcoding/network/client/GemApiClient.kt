package com.cdcoding.network.client

import com.cdcoding.common.utils.toIdentifier
import com.cdcoding.model.AssetFull
import com.cdcoding.model.AssetId
import com.cdcoding.model.AssetPrice
import com.cdcoding.model.AssetPricesRequest
import com.cdcoding.model.NameRecord
import com.cdcoding.model.StakeValidator
import com.cdcoding.model.Subscription
import com.cdcoding.network.model.FiatAssets
import com.cdcoding.network.model.PricesResponse
import com.cdcoding.network.util.NetworkError
import com.cdcoding.network.util.Result
import com.cdcoding.network.util.getResult
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class GemApiClient(
    private val httpClient: HttpClient
) {

    companion object {
        const val GEM_URL = "https://api.gemwallet.com"
    }

    suspend fun getFiatAssets(): Result<FiatAssets, NetworkError> {
        return httpClient.get("$GEM_URL/v1/fiat/on_ramp/assets").getResult()
    }

    suspend fun getAssets(deviceId: String, walletIndex: Int, fromTimestamp: Int = 0): Result<List<String>, NetworkError> {
        return httpClient.get("$GEM_URL/v1/assets/by_device_id/$deviceId") {
            parameter("wallet_index", walletIndex)
            parameter("from_timestamp", fromTimestamp)
        }
        .getResult()
    }

    suspend fun search(query: String): Result<List<AssetFull>, NetworkError> {
        return httpClient.get("$GEM_URL/v1/assets/search") {
            parameter("query", query)
        }
        .getResult()
    }


    suspend fun getSubscriptions(deviceId: String): Result<List<Subscription>, NetworkError> {
        return httpClient.get("$GEM_URL/v1/subscriptions/$deviceId").getResult()
    }

    suspend fun addSubscriptions(deviceId: String, request: List<Subscription>): Result<Any, NetworkError> {
        return httpClient.post("$GEM_URL/v1/subscriptions/$deviceId"){
            contentType(ContentType.Application.Json)
            setBody(request)
        }.getResult()
    }

    suspend fun getPrices(currencyCode: String, assets: List<AssetId>): Result<PricesResponse, NetworkError> {
        val request = AssetPricesRequest(
            currency = currencyCode,
            assetIds = assets.map { it.toIdentifier() },
        )
        return httpClient.post("$GEM_URL/v1/prices"){
            contentType(ContentType.Application.Json)
            setBody(request)
        }.getResult()
    }


    suspend fun resolve( domain: String, chain: String): Result<NameRecord, NetworkError> {
        return httpClient.get("$GEM_URL/v1/name/resolve/$domain") {
            parameter("chain", chain)
        }.getResult()
    }

    suspend fun getValidators( chain:String): Result<List<StakeValidator>, NetworkError> {
        return httpClient.get("$GEM_URL/blockchains/$chain/validators.json").getResult()
    }
}