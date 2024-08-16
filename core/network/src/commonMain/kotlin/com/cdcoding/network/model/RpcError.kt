package com.cdcoding.network.model


sealed class RpcError(message: String) : Exception(message) {
    object TransactionSendError : RpcError("Transaction send fail")
    object BadResponse : RpcError("Bad response")
    class Error(message: String, code: Int) : RpcError("Code: $code - $message")

    class BroadcastFail(message: String) : RpcError(message)
}
