package com.cdcoding.core.designsystem.util

import androidx.compose.runtime.Composable
import com.cdcoding.common.utils.getAddressEllipsisText
import com.cdcoding.core.resource.Res
import com.cdcoding.core.resource.transfer_stake_title
import com.cdcoding.core.resource.transfer_unstake_title
import com.cdcoding.core.resource.transfer_redelegate_title
import com.cdcoding.core.resource.transfer_rewards_title
import com.cdcoding.core.resource.transfer_title
import com.cdcoding.core.resource.wallet_swap
import com.cdcoding.core.resource.transfer_approve_title
import com.cdcoding.core.resource.transfer_withdraw_title
import com.cdcoding.core.resource.transfer_to
import com.cdcoding.core.resource.transfer_from
import com.cdcoding.model.TransactionDirection
import com.cdcoding.model.TransactionType
import org.jetbrains.compose.resources.stringResource

@Composable
fun TransactionType.getTitle(): String {
    return when (this) {
        TransactionType.StakeDelegate -> stringResource(Res.string.transfer_stake_title)
        TransactionType.StakeUndelegate -> stringResource(Res.string.transfer_unstake_title)
        TransactionType.StakeRedelegate -> stringResource(Res.string.transfer_redelegate_title)
        TransactionType.StakeRewards -> stringResource(Res.string.transfer_rewards_title)
        TransactionType.Transfer -> stringResource(Res.string.transfer_title)
        TransactionType.Swap -> stringResource(Res.string.wallet_swap)
        TransactionType.TokenApproval -> stringResource(Res.string.transfer_approve_title)
        TransactionType.StakeWithdraw -> stringResource(Res.string.transfer_withdraw_title)
    }
}

@Composable
fun TransactionType.getTransactionTitle(assetSymbol: String): String {
    return when (this) {
        TransactionType.StakeDelegate,
        TransactionType.StakeUndelegate,
        TransactionType.StakeRewards,
        TransactionType.StakeRedelegate,
        TransactionType.StakeWithdraw,
        TransactionType.Transfer,
        TransactionType.Swap -> getTitle()
        TransactionType.TokenApproval -> "${stringResource(Res.string.transfer_approve_title)} $assetSymbol"
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
        TransactionType.TokenApproval,
        TransactionType.StakeDelegate,
        TransactionType.StakeUndelegate,
        TransactionType.StakeRedelegate,
        TransactionType.StakeWithdraw,
        TransactionType.StakeRewards -> ""
    }
}