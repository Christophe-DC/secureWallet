package com.cdcoding.network.client.near

enum class NearMethod(val value: String) {
    GasPrice("gas_price"),
    Query("query"),
    LatestBlock("block"),
    Transaction("tx"),
    Broadcast("send_tx"),
}