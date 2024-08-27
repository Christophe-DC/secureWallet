package com.cdcoding.core.designsystem.util

import androidx.compose.runtime.Composable
import com.cdcoding.common.utils.getAddressEllipsisText
import com.cdcoding.core.resource.Res
import com.cdcoding.core.resource.transfer_approve_title
import com.cdcoding.core.resource.transfer_from
import com.cdcoding.core.resource.transfer_title
import com.cdcoding.core.resource.transfer_to
import com.cdcoding.core.resource.wallet_swap
import com.cdcoding.model.TransactionDirection
import com.cdcoding.model.TransactionType
import org.jetbrains.compose.resources.stringResource

@Composable
fun TransactionType.getTitle(): String {
    return when (this) {
        TransactionType.Transfer -> stringResource(Res.string.transfer_title)
        TransactionType.Swap -> stringResource(Res.string.wallet_swap)
        TransactionType.TokenApproval -> stringResource(Res.string.transfer_approve_title)
        else -> stringResource(Res.string.transfer_title)
    }
}

@Composable
fun TransactionType.getTransactionTitle(assetSymbol: String): String {
    return when (this) {
        TransactionType.Transfer,
        TransactionType.Swap -> getTitle()
        TransactionType.TokenApproval -> "${stringResource(Res.string.transfer_approve_title)} $assetSymbol"
        else -> ""
    }
}

@Composable
fun TransactionType.getAddress(direction: TransactionDirection, from: String, to: String): String {
    return when (this) {
        TransactionType.Transfer -> when (direction) {
            TransactionDirection.SelfTransfer,
            TransactionDirection.Outgoing -> "${stringResource(Res.string.transfer_to)} ${to.getAddressEllipsisText()}"
            TransactionDirection.Incoming -> "${stringResource(Res.string.transfer_from)} ${from.getAddressEllipsisText()}"
        }
        TransactionType.Swap,
        TransactionType.TokenApproval -> ""
        else -> ""
    }
}