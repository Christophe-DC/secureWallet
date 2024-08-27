package com.cdcoding.database.mapper


import com.cdcoding.common.utils.toAssetId
import com.cdcoding.database.db.model.asExternal
import com.cdcoding.local.db.GetExtendedTransactions
import com.cdcoding.local.db.TokenEntity
import com.cdcoding.model.Asset
import com.cdcoding.model.Price
import com.cdcoding.model.Transaction
import com.cdcoding.model.TransactionExtended


fun GetExtendedTransactions.toExtendedTransaction(): TransactionExtended? {
    return TransactionExtended(
        transaction = Transaction(
            id = this.id,
            hash = this.hash,
            assetId = this.assetId.toAssetId() ?: return null,
            from = this.owner,
            to = this.recipient,
            contract = this.contract,
            type = this.type.asExternal(),
            state = this.state.asExternal(),
            blockNumber = this.blockNumber,
            sequence = this.sequence,
            fee = this.fee,
            feeAssetId = this.feeAssetId.toAssetId() ?: return null,
            value = this.value_,
            memo = this.payload,
            direction = this.direction.asExternal(),
            utxoInputs = emptyList(),
            utxoOutputs = emptyList(),
            createdAt = this.createdAt,
            metadata = this.metadata,
        ),
        asset = Asset(
            id = this.assetId.toAssetId() ?: return null,
            name = this.assetName,
            symbol = this.assetSymbol,
            decimals = this.assetDecimals.toInt(),
            type = this.assetType.asExternal(),
        ),
        feeAsset = Asset(
            id = this.feeAssetId.toAssetId() ?: return null,
            name = this.feeName,
            symbol = this.feeSymbol,
            decimals = this.feeDecimals.toInt(),
            type = this.feeType.asExternal(),
        ),
        price = if (this.assetPrice == null) null else Price(this.assetPrice, this.assetPriceChanged ?: 0.0),
        feePrice = if (this.feePrice == null) null else Price(this.feePrice, this.feePriceChanged ?: 0.0),
        assets = emptyList(),
    )
}