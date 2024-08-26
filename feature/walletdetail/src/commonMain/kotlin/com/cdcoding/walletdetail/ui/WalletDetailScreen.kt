package com.cdcoding.walletdetail.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.cdcoding.core.designsystem.components.AssetList
import com.cdcoding.core.designsystem.components.WalletDetailHeader
import com.cdcoding.core.designsystem.components.WalletDetailHeaderActions
import com.cdcoding.core.designsystem.hooks.useInject
import com.cdcoding.core.designsystem.state.collectAsStateWithLifecycle
import com.cdcoding.core.navigation.SelectAssetDestination
import com.cdcoding.model.AssetId
import com.cdcoding.model.AssetUIState
import com.cdcoding.model.SelectAssetType
import com.cdcoding.model.TransactionExtended
import com.cdcoding.walletdetail.presentation.WalletDetailIntent
import com.cdcoding.walletdetail.presentation.WalletDetailViewModel
import com.cdcoding.walletdetail.presentation.WalletInfoUIState
import kotlinx.collections.immutable.ImmutableList

class WalletDetailScreen : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Filled.AccountBalanceWallet)
            val viewModel: WalletDetailViewModel = useInject()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            val title =  uiState.session?.wallet?.name ?: ""
            return remember(title) {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val viewModel: WalletDetailViewModel = useInject()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        val selectSendAssetDestinationEvent = rememberScreen(SelectAssetDestination.SelectAsset(SelectAssetType.Send))
        val selectReceivedAssetDestinationEvent = rememberScreen(SelectAssetDestination.SelectAsset(SelectAssetType.Receive))

        WalletDetailScreenContent(
            isLoading = uiState.isLoading,
            walletInfo = uiState.walletInfo,
            assets = uiState.assets,
            transactions = uiState.pendingTransactions,
            onIntent = viewModel::setIntent,
            onSendClick = {
                navigator.parent?.push(selectSendAssetDestinationEvent)
            },
            onReceiveClick = {
                navigator.parent?.push(selectReceivedAssetDestinationEvent)
            }
        )
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WalletDetailScreenContent(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    walletInfo: WalletInfoUIState,
    assets: ImmutableList<AssetUIState>,
    transactions: ImmutableList<TransactionExtended>,
    onIntent: (WalletDetailIntent) -> Unit,
    onShowAssetManage: () -> Unit = {},
    onSendClick: () -> Unit = {},
    onReceiveClick: () -> Unit = {},
    onTransactionClick: (String) -> Unit = {},
    onAssetClick: (AssetId) -> Unit = {},
    listState: LazyListState = rememberLazyListState()
) {
    val pullRefreshState =
        rememberPullRefreshState(isLoading, { onIntent(WalletDetailIntent.OnRefresh) })
    Box(
        modifier = modifier.pullRefresh(pullRefreshState),
    ) {
        AssetList(
            headerItem = {
                WalletDetailHeader(
                    amount = walletInfo.totalValue,
                    actions = {
                        WalletDetailHeaderActions(
                            walletType = walletInfo.type,
                            onTransfer = onSendClick,
                            transferEnabled = true,
                            onReceive = onReceiveClick
                        )
                    }
                )
            },
            assets = assets,
            transactions = transactions,
            onShowAssetManage = onShowAssetManage,
            onTransactionClick = onTransactionClick,
            onAssetClick = onAssetClick,
            listState = listState,
        )
        PullRefreshIndicator(isLoading, pullRefreshState, Modifier.align(Alignment.TopCenter))
    }
}