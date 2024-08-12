package com.cdcoding.network.client.cosmo

import com.cdcoding.model.StakeValidator
import com.cdcoding.model.cosmos.CosmosDelegations
import com.cdcoding.model.cosmos.CosmosRewards
import com.cdcoding.model.cosmos.CosmosUnboundingDelegations
import com.cdcoding.model.cosmos.CosmosValidators
import com.cdcoding.network.client.GemApiClient.Companion.GEM_URL
import com.cdcoding.network.util.NetworkError
import com.cdcoding.network.util.getResult
import com.cdcoding.network.util.Result
import io.ktor.client.HttpClient
import io.ktor.client.request.get


class CosmosApiClient(
    private val httpClient: HttpClient
) {

    private val COMSMOS_URL = "https://cosmos.gemnodes.com"


    suspend fun getValidators(): Result<CosmosValidators, NetworkError> {
        return httpClient.get("$COMSMOS_URL/cosmos/staking/v1beta1/validators?pagination.limit=1000").getResult()
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


    /*@GET("/cosmos/bank/v1beta1/balances/{owner}")
    suspend fun getBalance(@Path("owner") owner: String): Result<CosmosBalances>

    @GET("/cosmos/auth/v1beta1/accounts/{owner}")
    suspend fun getAccountData(@Path("owner") owner: String): Result<CosmosAccountResponse<CosmosAccount>>

    @GET("/cosmos/auth/v1beta1/accounts/{owner}")
    suspend fun getInjectiveAccountData(@Path("owner") owner: String): Result<CosmosAccountResponse<CosmosInjectiveAccount>>

    @GET("/cosmos/base/tendermint/v1beta1/blocks/latest")
    suspend fun getNodeInfo(): Result<CosmosBlockResponse>

    @POST("/cosmos/tx/v1beta1/txs")
    suspend fun broadcast(@Body body: RequestBody): Result<CosmosBroadcastResponse>

    @GET("/cosmos/tx/v1beta1/txs/{txId}")
    suspend fun transaction(@Path("txId") txId: String): Result<CosmosTransactionResponse>

    @GET("/cosmos/staking/v1beta1/validators?pagination.limit=1000")
    suspend fun validators(): Result<CosmosValidators>

    @GET("/cosmos/staking/v1beta1/delegations/{address}")
    suspend fun delegations(@Path("address") address: String): Result<CosmosDelegations>

    @GET("/cosmos/staking/v1beta1/delegators/{address}/unbonding_delegations")
    suspend fun undelegations(@Path("address") address: String): Result<CosmosUnboundingDelegations>

    @GET("/cosmos/distribution/v1beta1/delegators/{address}/rewards")
    suspend fun rewards(@Path("address") address: String): Result<CosmosRewards>*/

}