package com.cdcoding.network.client.bitcoin

import com.cdcoding.model.bitcoin.BitcoinAccount
import com.cdcoding.model.bitcoin.BitcoinFeeResult
import com.cdcoding.model.bitcoin.BitcoinTransaction
import com.cdcoding.model.bitcoin.BitcoinTransactionBroacastResult
import com.cdcoding.model.bitcoin.BitcoinUTXO
import com.cdcoding.network.util.NetworkError
import com.cdcoding.network.util.Result
import com.cdcoding.network.util.getResult
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class BitcoinApiClient(
    private val httpClient: HttpClient
) {

    private val BITCOIN_URL = "https://bitcoin.gemnodes.com"

    suspend fun getBalance(address: String): Result<BitcoinAccount, NetworkError> {
        return httpClient.get("$BITCOIN_URL/api/v2/address/$address").getResult()
    }

    suspend fun getUTXO(address: String): Result<List<BitcoinUTXO>, NetworkError> {
        return httpClient.get("$BITCOIN_URL/api/v2/utxo/$address").getResult()
    }


    suspend fun broadcast(body: ByteArray): Result<BitcoinTransactionBroacastResult, NetworkError> {
        return httpClient.post("$BITCOIN_URL/api/v2/sendtx/"){
            contentType(ContentType.Application.Json)
            setBody(body)
        }.getResult()
    }

    suspend fun estimateFee(priority: String): Result<BitcoinFeeResult, NetworkError> {
        return httpClient.get("$BITCOIN_URL/api/v2/estimatefee/$priority").getResult()
    }

    suspend fun transaction(txId: String): Result<BitcoinTransaction, NetworkError> {
        return httpClient.get("$BITCOIN_URL/api/v2/tx/$txId").getResult()
    }

}