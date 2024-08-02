package com.cdcoding.network.client

import com.cdcoding.model.Asset


interface GetTokenClient : BlockchainClient {
    suspend fun getTokenData(tokenId: String): Asset?

    suspend fun isTokenQuery(query: String): Boolean
}