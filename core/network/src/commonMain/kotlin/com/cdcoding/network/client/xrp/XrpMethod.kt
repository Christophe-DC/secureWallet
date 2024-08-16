package com.cdcoding.network.client.xrp

import com.cdcoding.network.model.JSONRpcMethod

enum class XrpMethod(val value: String) : JSONRpcMethod {
    Account("account_info"),
    Fee("fee"),
    Transaction("tx"),
    Broadcast("submit");

    override fun value(): String = value
}