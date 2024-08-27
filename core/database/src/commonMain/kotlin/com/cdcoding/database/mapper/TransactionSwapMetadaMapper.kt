package com.cdcoding.database.mapper


import com.cdcoding.common.utils.toAssetId
import com.cdcoding.common.utils.toIdentifier
import com.cdcoding.database.db.model.asExternal
import com.cdcoding.local.db.AssetEntity
import com.cdcoding.local.db.TxSwapMetadataEntity
import com.cdcoding.model.Asset
import com.cdcoding.model.AssetId
import com.cdcoding.model.AssetMetaData
import com.cdcoding.model.TransactionSwapMetadata

fun TransactionSwapMetadata.asEntity(txId: String): TxSwapMetadataEntity {
    return TxSwapMetadataEntity(
        txId = txId,
        fromAssetId = fromAsset.toIdentifier(),
        toAssetId = toAsset.toIdentifier(),
        fromAmount = fromValue,
        toAmount = toValue,
    )
}

fun TxSwapMetadataEntity.asExternal(): TransactionSwapMetadata? {
    val fromAsset = fromAssetId.toAssetId() ?: return null
    val toAssetId = toAssetId.toAssetId() ?: return null
    return TransactionSwapMetadata(
        fromAsset = fromAsset,
        toAsset = toAssetId,
        fromValue = fromAmount,
        toValue = toAmount,
    )
}

