package com.cdcoding.network.client.cosmo

import com.cdcoding.model.AssetId
import com.cdcoding.model.Chain
import com.cdcoding.model.Fee
import com.cdcoding.model.GasFee
import com.cdcoding.model.TransactionType
import com.ionspin.kotlin.bignum.integer.BigInteger


class  CosmosFee(
    private val txType: TransactionType,
) {
    operator fun invoke(chain: Chain): Fee {
        val assetId = AssetId(chain)
        val maxGasFee = when (chain) {
            Chain.Cosmos -> when (txType) {
                TransactionType.Transfer, TransactionType.Swap -> BigInteger(5_000L)
                else -> BigInteger(17_000L)
            }
            Chain.Osmosis -> when (txType) {
                TransactionType.Transfer, TransactionType.Swap -> BigInteger(50_000L)
                else -> BigInteger(100_000L)
            }

            Chain.Thorchain -> BigInteger(4_000_000)
            Chain.Celestia -> when (txType){
                TransactionType.Transfer, TransactionType.Swap -> BigInteger(3_000L)
                else -> BigInteger(10_000L)
            }
            Chain.Injective -> when (txType){
                TransactionType.Transfer, TransactionType.Swap -> BigInteger(1_000_000_000_000_000L)
                else -> BigInteger(500_000_000_000_000L)
            }
            Chain.Sei -> when (txType){
                TransactionType.Transfer, TransactionType.Swap -> BigInteger(200_000L)
                else -> BigInteger(100_000L)
            }
            Chain.Noble -> BigInteger(25_000)
            else -> throw IllegalArgumentException()
        }
        val limit = when (txType) {
            TransactionType.Transfer -> BigInteger(200_000L)
            TransactionType.Swap,
            TransactionType.TokenApproval -> throw IllegalArgumentException()
        }
        return GasFee(
            feeAssetId = assetId,
            maxGasPrice = maxGasFee,
            amount = maxGasFee,
            limit = limit,
        )
    }
}