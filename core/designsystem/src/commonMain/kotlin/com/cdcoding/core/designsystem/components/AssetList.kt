package com.cdcoding.core.designsystem.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.cdcoding.common.utils.getIconUrl
import com.cdcoding.common.utils.getSupportIconUrl
import com.cdcoding.common.utils.getSwapMetadata
import com.cdcoding.common.utils.toIdentifier
import com.cdcoding.core.designsystem.badge.SWBadge
import com.cdcoding.core.resource.Res
import com.cdcoding.core.resource.common_hide
import com.cdcoding.core.resource.wallet_copy_address
import com.cdcoding.core.resource.wallet_manage_token_list
import com.cdcoding.model.AssetId
import com.cdcoding.model.AssetType
import com.cdcoding.model.Chain
import com.cdcoding.model.Crypto
import com.cdcoding.model.TransactionExtended
import com.cdcoding.model.AssetUIState
import com.cdcoding.model.PriceUIState
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.stringResource


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AssetList(
    headerItem: (@Composable () -> Unit)? = null,
    assets: ImmutableList<AssetUIState>,
    transactions: ImmutableList<TransactionExtended>,
    onShowAssetManage: () -> Unit,
    onTransactionClick: (String) -> Unit,
    onAssetClick: (AssetId) -> Unit,
    listState: LazyListState,
) {

    val clipboardManager = LocalClipboardManager.current
    var longPressedAsset by remember {
        mutableStateOf<AssetId?>(null)
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
    ) {
        if (headerItem != null) {
            item {
                headerItem()
            }
        }
        transactionsList(transactions) { onTransactionClick(it) }
        if (transactions.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(thickness = 0.4.dp)
            }
        }
        items(items = assets, key = { it.id.toIdentifier() }) { asset ->

            var itemWidth by remember { mutableIntStateOf(0) }
            Box(
                modifier = Modifier.onSizeChanged { itemWidth = it.width }
            ) {
                AssetListItem(
                    chain = asset.id.chain,
                    title = asset.name,
                    iconUrl = asset.icon,
                    value = asset.value,
                    assetType = asset.type,
                    isZeroValue = asset.isZeroValue,
                    fiatAmount = asset.fiat,
                    price = asset.price,
                    modifier = Modifier.combinedClickable(
                        onClick = { onAssetClick(asset.id) },
                        onLongClick = { longPressedAsset = asset.id },
                    )
                )
                DropdownMenu(
                    expanded = longPressedAsset == asset.id,
                    offset = DpOffset((with(LocalDensity.current) { itemWidth.toDp() } / 2), 8.dp),
                    onDismissRequest = { longPressedAsset = null },
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = stringResource(Res.string.wallet_copy_address),
                            )
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "address_copy"
                            )
                        },
                        onClick = {
                            clipboardManager.setText(AnnotatedString(asset.owner))
                            longPressedAsset = null
                        },
                    )
                }
            }
        }
        item {
            Box(
                modifier = Modifier
                    .clickable(onClick = onShowAssetManage)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = "asset_manager",
                    )
                    Spacer(modifier = Modifier.size(ButtonDefaults.IconSize))
                    Text(
                        text = stringResource(Res.string.wallet_manage_token_list),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.transactionsList(
    items: List<TransactionExtended>,
    onTransactionClick: (String) -> Unit
) {
    //val dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM)
    var prev = 0L
    //val calendar = Calendar.getInstance()

    items.forEachIndexed { index, item ->
        val createdAt = item.transaction.createdAt
        if (prev != createdAt) {
            stickyHeader {
                /*val title = if (DateUtils.isToday(createdAt) || DateUtils.isToday(createdAt + DateUtils.DAY_IN_MILLIS)) {
                    DateUtils.getRelativeTimeSpanString(item.transaction.createdAt, System.currentTimeMillis(), DateUtils.DAY_IN_MILLIS).toString()
                } else {
                    dateFormat.format(Date(item.transaction.createdAt))
                }*/
            }
        }
        item(key = item.transaction.id) {
            TransactionItem(
                assetIcon = item.asset.getIconUrl(),
                supportIcon = item.asset.getSupportIconUrl(),
                assetSymbol = item.asset.symbol,
                to = item.transaction.to,
                from = item.transaction.from,
                direction = item.transaction.direction,
                type = item.transaction.type,
                state = item.transaction.state,
                value = Crypto(item.transaction.value.toString()).format(
                    item.asset.decimals,
                    item.asset.symbol,
                    2,
                    dynamicPlace = true,
                ),
                metadata = item.transaction.getSwapMetadata(),
                assets = item.assets,
                isLast = index == items.size - 1
            ) { onTransactionClick(item.transaction.id) }
        }
        prev = createdAt
    }
}


@Composable
fun AssetListItem(
    chain: Chain,
    title: String,
    assetType: AssetType,
    iconUrl: String,
    value: String,
    isZeroValue: Boolean,
    fiatAmount: String,
    modifier: Modifier = Modifier,
    price: PriceUIState? = null,
    badge: String? = null,
) {
    AssetListItem(
        modifier = modifier,
        chain = chain,
        title = title,
        assetType = assetType,
        iconUrl = iconUrl,
        price = price,
        trailing = { getBalanceInfo(isZeroValue, value, fiatAmount) },
        badge = badge,
    )
}

@Composable
fun getBalanceInfo(isZeroValue: Boolean, value: String, fiatAmount: String) {
    if (isZeroValue) {
        ListItemTitle(
            modifier = Modifier.defaultMinSize(minHeight = 40.dp),
            title = value,
            color = MaterialTheme.colorScheme.secondary,
            subtitle = "",
            horizontalAlignment = Alignment.End,
        )
    } else {
        ListItemTitle(
            title = value,
            color = MaterialTheme.colorScheme.onSurface,
            subtitle = fiatAmount,
            horizontalAlignment = Alignment.End
        )
    }
}

@Composable
fun AssetListItem(
    chain: Chain,
    title: String,
    assetType: AssetType,
    iconUrl: String,
    modifier: Modifier = Modifier,
    price: PriceUIState? = null,
    badge: String? = null,
    trailing: (@Composable () -> Unit)? = null,
) {
    ListItem(
        modifier = modifier,
        iconUrl = iconUrl,
        supportIcon = if (assetType == AssetType.NATIVE) null else chain.getIconUrl(),
        placeholder = title[0].toString(),
        trailing = trailing
    ) {
        val priceInfo: (@Composable () -> Unit)? = if (price == null || price.value.isEmpty()) {
            null
        } else {
            {
                PriceInfo(
                    price = price,
                    style = MaterialTheme.typography.bodyMedium,
                    internalPadding = 4.dp
                )
            }
        }
        ListItemTitle(
            modifier = Modifier.fillMaxHeight(),
            title = title,
            titleBadge = { SWBadge(text = badge) },
            subtitle = priceInfo,
        )
    }
}

@Composable
fun AssetListItem(
    chain: Chain,
    title: String,
    support: String?,
    assetType: AssetType,
    iconUrl: String,
    modifier: Modifier = Modifier,
    dividerShowed: Boolean = true,
    badge: String? = null,
    trailing: (@Composable () -> Unit)? = null,
) {
    ListItem(
        modifier = modifier,
        iconUrl = iconUrl,
        supportIcon = if (assetType == AssetType.NATIVE) null else chain.getIconUrl(),
        placeholder = title[0].toString(),
        dividerShowed = dividerShowed,
        trailing = trailing
    ) {
        ListItemTitle(
            modifier = Modifier.fillMaxHeight(),
            title = title,
            titleBudge = { SWBadge(text = badge) },
            subtitle = support,
        )
    }
}


@Composable
fun ListItemTitle(
    title: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface,
    titleBadge: (@Composable () -> Unit)? = null,
    subtitle: (@Composable () -> Unit)? = null,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = horizontalAlignment,
    ) {
        if (title.isNotEmpty()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.weight(1f, false),
                    text = title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,
                    color = color,
                )
                titleBadge?.invoke()
            }
        }
        if (subtitle != null) {
            Spacer(modifier = Modifier.height(2.dp))
            subtitle()
        }
    }
}

@Composable
fun ListItemTitle(
    title: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface,
    titleBudge: (@Composable () -> Unit)? = null,
    subtitle: String? = null,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
) {
    val subtitleRender: (@Composable () -> Unit)? = if (subtitle.isNullOrEmpty()) {
        null
    } else {
        {
            Text(
                modifier = Modifier.padding(top = 0.dp, bottom = 2.dp),
                text = subtitle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
    ListItemTitle(
        modifier = modifier,
        title = title,
        color = color,
        titleBadge = titleBudge,
        subtitle = subtitleRender,
        horizontalAlignment = horizontalAlignment,
    )
}


@Composable
fun ListItem(
    iconUrl: String,
    modifier: Modifier = Modifier,
    supportIcon: String? = null,
    placeholder: String? = null,
    dividerShowed: Boolean = true,
    trailing: @Composable (() -> Unit)? = null,
    body: @Composable () -> Unit,
) {
    ListItem(
        modifier = modifier,
        dividerShowed = dividerShowed,
        leading = {
            AssetIcon(iconUrl = iconUrl, placeholderText = placeholder, supportIcon = supportIcon)
        },
        trailing = trailing,
        body = body,
    )
}

@Composable
fun ListItem(
    modifier: Modifier = Modifier,
    dividerShowed: Boolean = true,
    leading: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
    body: @Composable () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(modifier = Modifier.size(16.dp))
        if (leading != null) {
            leading()
            Spacer(modifier = Modifier.size(16.dp))
        }
        Box(modifier = Modifier.heightIn(72.dp).weight(1f)) {
            Row(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(top = 12.dp, end = 16.dp, bottom = 12.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    body()
                }
                if (trailing != null) {
                    Spacer(modifier = Modifier.size(16.dp))
                    trailing()
                }
            }
            if (dividerShowed) {
                HorizontalDivider(Modifier.align(Alignment.BottomStart), thickness = 0.4.dp)
            }
        }
    }
}
