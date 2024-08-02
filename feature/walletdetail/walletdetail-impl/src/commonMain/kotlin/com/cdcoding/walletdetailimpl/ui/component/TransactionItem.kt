package com.cdcoding.walletdetailimpl.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.cdcoding.common.utils.toIdentifier
import com.cdcoding.core.designsystem.circularProgress.CircularProgressIndicator10
import com.cdcoding.core.designsystem.util.getAddress
import com.cdcoding.core.designsystem.util.getTransactionTitle
import com.cdcoding.core.resource.Res
import com.cdcoding.core.resource.transaction_status_failed
import com.cdcoding.core.resource.transaction_status_pending
import com.cdcoding.core.resource.transaction_status_reverted
import com.cdcoding.model.Asset
import com.cdcoding.model.Crypto
import com.cdcoding.model.TransactionDirection
import com.cdcoding.model.TransactionState
import com.cdcoding.model.TransactionSwapMetadata
import com.cdcoding.model.TransactionType
import com.cdcoding.model.getValue
import org.jetbrains.compose.resources.stringResource


@Composable
fun TransactionItem(
    assetIcon: String,
    assetSymbol: String,
    type: TransactionType,
    state: TransactionState,
    value: String,
    from: String,
    to: String,
    direction: TransactionDirection,
    metadata: Any?,
    assets: List<Asset>,
    supportIcon: String? = null,
    isLast: Boolean = false,
    onClick: () -> Unit,
) {
    ListItem(
        modifier = Modifier
            .clickable(onClick = onClick)
            .heightIn(72.dp),
        iconUrl = assetIcon,
        supportIcon = supportIcon,
        placeholder = assetSymbol,
        dividerShowed = !isLast,
        trailing = {
            ListItemTitle(
                title = when (type) {
                    TransactionType.Swap -> {
                        val swapMetadata = metadata as? TransactionSwapMetadata
                        val toAssetId = swapMetadata?.toAsset?.toIdentifier()
                        val asset = assets.firstOrNull { it.id.toIdentifier() == toAssetId }
                        if (swapMetadata == null || asset == null) {
                            ""
                        } else {
                            "+${
                                Crypto(swapMetadata.toValue)
                                    .format(asset.decimals, asset.symbol, 2, dynamicPlace = true)
                            }"
                        }
                    }

                    else -> type.getValue(direction, value)
                },
                color = when (type) {
                    TransactionType.Swap -> MaterialTheme.colorScheme.tertiary
                    else -> when (direction) {
                        TransactionDirection.SelfTransfer,
                        TransactionDirection.Outgoing -> MaterialTheme.colorScheme.onSurface
                        TransactionDirection.Incoming -> MaterialTheme.colorScheme.tertiary
                    }
                },
                subtitle = when (type) {
                    TransactionType.Swap -> {
                        val swapMetadata = metadata as? TransactionSwapMetadata
                        val toAssetId = swapMetadata?.fromAsset?.toIdentifier()
                        val asset = assets.firstOrNull { it.id.toIdentifier() == toAssetId }
                        if (swapMetadata == null || asset == null) {
                            ""
                        } else {
                            "-${
                                Crypto(swapMetadata.fromValue)
                                    .format(asset.decimals, asset.symbol, 2, dynamicPlace = true)
                            }"
                        }
                    }

                    else -> ""
                },
                horizontalAlignment = Alignment.End,
            )
        },
        body = {
            ListItemTitle(
                title = type.getTransactionTitle(assetSymbol = assetSymbol),
                subtitle = type.getAddress(direction, from, to),
                titleBudge = {
                    val badge = when (state) {
                        TransactionState.Pending -> stringResource(Res.string.transaction_status_pending)
                        TransactionState.Confirmed -> ""
                        TransactionState.Failed -> stringResource(Res.string.transaction_status_failed)
                        TransactionState.Reverted -> stringResource(Res.string.transaction_status_reverted)
                    }
                    val color = when (state) {
                        TransactionState.Pending -> MaterialTheme.colorScheme.tertiary
                        TransactionState.Confirmed -> MaterialTheme.colorScheme.secondary
                        TransactionState.Reverted,
                        TransactionState.Failed -> MaterialTheme.colorScheme.error
                    }
                    Row(
                        Modifier
                            .padding(start = 5.dp)
                            .background(
                                color = color.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(6.dp)
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (badge.isNotEmpty()) {
                            Text(
                                modifier = Modifier.padding(
                                    start = 5.dp,
                                    top = 2.dp,
                                    end = 4.dp,
                                    bottom = 2.dp
                                ),
                                text = badge,
                                color = color,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.labelMedium,
                            )
                            if (state == TransactionState.Pending) {
                                CircularProgressIndicator10(color = color)
                                Spacer(modifier = Modifier.size(8.dp))
                            }
                        }
                    }
                }
            )
        },
    )
}