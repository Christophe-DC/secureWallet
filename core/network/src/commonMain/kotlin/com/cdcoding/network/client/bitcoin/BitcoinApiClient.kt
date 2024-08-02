package com.cdcoding.network.client.bitcoin

import com.cdcoding.network.client.GemApiClient.Companion.GEM_URL
import com.cdcoding.network.model.BitcoinAccount
import com.cdcoding.network.util.NetworkError
import com.cdcoding.network.util.getResult
import com.cdcoding.network.util.Result
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class BitcoinApiClient(
    private val httpClient: HttpClient
) {

    private val BITCOIN_URL = "https://bitcoin.gemnodes.com"

    suspend fun getBalance(address: String): Result<BitcoinAccount, NetworkError> {
        return httpClient.get("$BITCOIN_URL/api/v2/address/$address").getResult()
    }
   /* @GET("/api/v2/address/{address}")
    suspend fun getBalance(@Path("address") address: String): Result<BitcoinAccount>

    @GET("/api/v2/utxo/{address}")
    suspend fun getUTXO(@Path("address") address: String): Result<List<BitcoinUTXO>>

    @GET("/api/v2/estimatefee/{priority}")
    suspend fun estimateFee(@Path("priority") priority: String): Result<BitcoinFeeResult>

    @POST("/api/v2/sendtx/")
    suspend fun broadcast(@Body body: RequestBody): Result<BitcoinTransactionBroacastResult>

    @GET("/api/v2/tx/{txId}")
    suspend fun transaction(@Path("txId") txId: String): Result<BitcoinTransaction>*/
}