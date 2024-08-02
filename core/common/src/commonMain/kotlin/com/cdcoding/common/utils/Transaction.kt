package com.cdcoding.common.utils

import com.cdcoding.model.AssetId
import com.cdcoding.model.Chain
import com.cdcoding.model.Transaction
import com.cdcoding.model.TransactionSwapMetadata
import com.cdcoding.model.TransactionType
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

fun Transaction.getSwapMetadata(): TransactionSwapMetadata? {
    if (type != TransactionType.Swap || metadata.isNullOrEmpty()) {
        return null
    }

    return try {
        val json = Json.parseToJsonElement(metadata!!).jsonObject

        val fromAsset = json["fromAsset"]?.jsonObject ?: return null
        val toAsset = json["toAsset"]?.jsonObject ?: return null

        val fromAssetId = AssetId(
            chain = Chain.findByString(fromAsset["chain"]?.jsonPrimitive?.content ?: return null) ?: return null,
            tokenId = fromAsset["tokenId"]?.jsonPrimitive?.contentOrNull
        )

        val toAssetId = AssetId(
            chain = Chain.findByString(toAsset["chain"]?.jsonPrimitive?.content ?: return null) ?: return null,
            tokenId = toAsset["tokenId"]?.jsonPrimitive?.contentOrNull
        )

        TransactionSwapMetadata(
            fromAsset = fromAssetId,
            toAsset = toAssetId,
            fromValue = json["fromValue"]?.jsonPrimitive?.content ?: return null,
            toValue = json["toValue"]?.jsonPrimitive?.content ?: return null
        )
    } catch (e: Exception) {
        null
    }
}