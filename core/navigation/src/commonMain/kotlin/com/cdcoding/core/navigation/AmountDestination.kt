package com.cdcoding.core.navigation

import com.cdcoding.core.navigation.core.Destination
import com.cdcoding.model.AssetId
import com.cdcoding.model.TransactionType


sealed interface AmountDestination : Destination {
    data class Amount(
        val assetId: AssetId,
        val destinationAddress: String,
        val addressDomain: String,
        val memo: String,
        val txType: TransactionType = TransactionType.Transfer,
    ) : AmountDestination
}