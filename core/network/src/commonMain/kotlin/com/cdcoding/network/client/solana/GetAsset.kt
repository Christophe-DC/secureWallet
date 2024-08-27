package com.cdcoding.network.client.solana

import com.cdcoding.model.Asset
import com.cdcoding.model.AssetId

fun interface GetAsset {
    suspend fun getAsset(assetId: AssetId): Asset?
}