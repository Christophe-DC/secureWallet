package com.cdcoding.network.client.ton

import com.cdcoding.network.model.JSONRpcRequest
import com.cdcoding.network.util.getOrNull


internal suspend fun jettonAddress(apiClient: TonApiClient, tokenId: String, address: String): String? {
    val response = apiClient.getJetonAddress(
        JSONRpcRequest(
            method = "runGetMethod",
            params = mapOf(
                "address" to tokenId,
                "method" to "get_wallet_address"
            )
        )
    )
    val result = response.getOrNull() ?: return null
    return result.b64 // todo decode
}