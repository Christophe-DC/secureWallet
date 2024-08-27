package com.cdcoding.core.navigation

import com.cdcoding.core.navigation.core.Destination
import com.cdcoding.model.AssetId
import com.cdcoding.model.ConfirmParams
import com.cdcoding.model.TransactionType


sealed interface ConfirmDestination : Destination {
    data class Confirm(
        val confirmParams: ConfirmParams,
        val txType: TransactionType = TransactionType.Transfer,
    ) : ConfirmDestination
}