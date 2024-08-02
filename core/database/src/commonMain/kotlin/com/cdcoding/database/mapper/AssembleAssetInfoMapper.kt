package com.cdcoding.database.mapper

import com.cdcoding.common.utils.toAssetId
import com.cdcoding.database.db.model.asExternal
import com.cdcoding.local.db.AssembleAssetInfo
import com.cdcoding.model.Account
import com.cdcoding.model.Asset
import com.cdcoding.model.AssetInfo
import com.cdcoding.model.AssetMetaData
import com.cdcoding.model.Balances


class AssembleAssetInfoMapper() {
    fun asExternal(entity: List<AssembleAssetInfo>): List<AssetInfo> {

        return entity.groupBy { it.id }.mapNotNull { records ->
            val first = records.value.firstOrNull() ?: return@mapNotNull null
            val assetId = first.id.toAssetId() ?: return@mapNotNull null

            val balances = Balances(
                /*records.value.mapNotNull {
                    if (it.amount != null && it.balanceType != null) {
                        AssetBalance(assetId, Balance(it.balanceType, it.amount))
                    } else {
                        null
                    }
                }*/
            )

            val currency = null // Currency.entries.firstOrNull { it.string == first.priceCurrency }

            AssetInfo(
                owner = Account(
                    id = first.id_,
                    chain = first.chain.asExternal(),
                    address = first.address,
                    derivationPath = first.derivationPath,
                    extendedPublicKey = first.extendedPublicKey,
                ),
                asset = Asset(
                    id = assetId,
                    name = first.name,
                    symbol = first.symbol,
                    decimals = first.decimals.toInt(),
                    type = first.type.asExternal(),
                ),
                balances = balances,
                price = /*if (first.priceValue != null && currency != null) {
                    AssetPriceInfo(
                        currency = currency,
                        price = AssetPrice(
                            assetId = assetId.toIdentifier(),
                            price = first.priceValue,
                            priceChangePercentage24h = first.priceDayChanges ?: 0.0,
                        )
                    )
                } else */ null,
                metadata = AssetMetaData(
                    isEnabled = first.is_visible == 1L,
                    isBuyEnabled = first.is_buy_enabled == 1L,
                    isSwapEnabled = first.is_swap_enabled == 1L,
                    isStakeEnabled = first.is_stake_enabled == 1L,
                ),
                links = /*if (first.links != null) gson.fromJson(
                    first.links,
                    AssetLinks::class.java
                ) else*/ null,
                market = /*if (first.market != null) gson.fromJson(
                    first.market,
                    AssetMarket::class.java
                ) else */ null,
                rank = first.rank.toInt(),
                walletName = first.walletName,
                walletType = first.walletType.asExternal(),
                stakeApr = null //first.stakingApr,
            )

        }
    }


}