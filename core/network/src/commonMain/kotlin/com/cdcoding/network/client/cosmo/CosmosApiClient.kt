package com.cdcoding.network.client.cosmo

import com.cdcoding.model.StakeValidator
import com.cdcoding.model.bitcoin.BitcoinTransactionBroacastResult
import com.cdcoding.model.cosmos.CosmosAccount
import com.cdcoding.model.cosmos.CosmosAccountResponse
import com.cdcoding.model.cosmos.CosmosBlockResponse
import com.cdcoding.model.cosmos.CosmosBroadcastResponse
import com.cdcoding.model.cosmos.CosmosDelegations
import com.cdcoding.model.cosmos.CosmosInjectiveAccount
import com.cdcoding.model.cosmos.CosmosRewards
import com.cdcoding.model.cosmos.CosmosUnboundingDelegations
import com.cdcoding.model.cosmos.CosmosValidators
import com.cdcoding.network.client.GemApiClient.Companion.GEM_URL
import com.cdcoding.network.util.NetworkError
import com.cdcoding.network.util.getResult
import com.cdcoding.network.util.Result
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType


class CosmosApiClient(
    private val httpClient: HttpClient
) {

    private val COMSMOS_URL = "https://cosmos.gemnodes.com"


    suspend fun getValidators(): Result<CosmosValidators, NetworkError> {
        return httpClient.get("$COMSMOS_URL/cosmos/staking/v1beta1/validators?pagination.limit=1000").getResult()
    }

    suspend fun getAccountData(owner: String): Result<CosmosAccountResponse<CosmosAccount>, NetworkError> {
        return httpClient.get("$COMSMOS_URL/cosmos/auth/v1beta1/accounts/$owner").getResult()
    }

    suspend fun getInjectiveAccountData(owner: String): Result<CosmosAccountResponse<CosmosInjectiveAccount>, NetworkError> {
        return httpClient.get("$COMSMOS_URL/cosmos/auth/v1beta1/accounts/$owner").getResult()
    }

    suspend fun getNodeInfo(): Result<CosmosBlockResponse, NetworkError> {
        return httpClient.get("$COMSMOS_URL/cosmos/base/tendermint/v1beta1/blocks/latest").getResult()
    }

    suspend fun broadcast(body: ByteArray): Result<CosmosBroadcastResponse, NetworkError> {
        return httpClient.post("$COMSMOS_URL/cosmos/tx/v1beta1/txs"){
            contentType(ContentType.Application.Json)
            setBody(body)
        }.getResult()
    }


    suspend fun getDelegations(address: String): Result<CosmosDelegations, NetworkError> {
        return httpClient.get("$COMSMOS_URL/cosmos/staking/v1beta1/delegations/$address").getResult()
    }

    suspend fun getUndelegations(address: String): Result<CosmosUnboundingDelegations, NetworkError> {
        return httpClient.get("$COMSMOS_URL/cosmos/staking/v1beta1/delegators/$address/unbonding_delegations").getResult()
    }

    suspend fun getRewards(address: String): Result<CosmosRewards, NetworkError> {
        return httpClient.get("$COMSMOS_URL/cosmos/distribution/v1beta1/delegators/$address/rewards").getResult()
    }

}