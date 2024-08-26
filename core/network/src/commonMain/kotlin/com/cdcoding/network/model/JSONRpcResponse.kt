package com.cdcoding.network.model

import kotlinx.serialization.Serializable

@Serializable
data class JSONRpcResponse<T>(
    val result: T,
    val error: JSONRpcError? = null,
)

@Serializable
data class JSONRpcError(
    val message: String,
)