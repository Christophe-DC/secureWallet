package com.cdcoding.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cdcoding.core.resource.Res
import com.cdcoding.core.resource.wallet_buy
import com.cdcoding.core.resource.wallet_receive
import com.cdcoding.core.resource.wallet_send
import com.cdcoding.core.resource.wallet_swap
import com.cdcoding.core.resource.wallet_watch_tooltip_title
import com.cdcoding.model.WalletType
import org.jetbrains.compose.resources.stringResource


@Composable
fun WalletDetailHeaderActions(
    walletType: WalletType,
    onTransfer: (() -> Unit)?,
    transferEnabled: Boolean,
    onReceive: (() -> Unit)?,
    onBuy: (() -> Unit)?,
    onSwap: (() -> Unit)?,
) {
    if (walletType == WalletType.view) {
        AssetWatchOnly()
        return
    }
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (onTransfer != null) {
            WalletDetailAction(
                title = stringResource(Res.string.wallet_send),
                imageVector = Icons.Default.ArrowUpward,
                contentDescription = "send",
                enabled = transferEnabled,
                onClick = onTransfer,
            )
        }
        if (onReceive != null) {
            WalletDetailAction(
                title = stringResource(Res.string.wallet_receive),
                imageVector = Icons.Default.ArrowDownward,
                contentDescription = "receive",
                onClick = onReceive,
            )
        }
        if (onBuy != null) {
            WalletDetailAction(
                title = stringResource(Res.string.wallet_buy),
                imageVector = Icons.Default.Add,
                contentDescription = "buy",
                onClick = onBuy,
            )
        }
        if (onSwap != null) {
            WalletDetailAction(
                title = stringResource(Res.string.wallet_swap),
                imageVector = Icons.Default.SwapVert,
                contentDescription = "swap",
                onClick = onSwap,
            )
        }
    }
}



@Composable
private fun WalletDetailAction(
    title: String,
    imageVector: ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    TextButton(modifier = modifier, onClick = onClick, enabled = enabled) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.surfaceContainerLow.copy(
                            alpha = if (enabled) 1f else 0.4f,
                        ),
                        shape = CircleShape
                    )
                    .padding(16.dp),
                imageVector = imageVector,
                tint = MaterialTheme.colorScheme.onSurface.copy(
                    alpha = if (enabled) 1f else 0.4f,
                ),
                contentDescription = contentDescription,
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.W400),
                maxLines = 1,
            )
        }
    }
}

@Composable
private fun AssetWatchOnly() {
    val uriHandler = LocalUriHandler.current
    Button(
        onClick = {},
        enabled = false,
        colors = ButtonDefaults
            .buttonColors(
                disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.07f)
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Default.Visibility,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = stringResource(Res.string.wallet_watch_tooltip_title),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
            )
            Spacer(modifier = Modifier.size(8.dp))
            IconButton(
                modifier = Modifier.size(24.dp),
                onClick = {
                    //  uriHandler.openUri(Config().getDocsUrl(DocsUrl.WHAT_IS_WATCH_WALLET))
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                )
            }
        }
    }
}