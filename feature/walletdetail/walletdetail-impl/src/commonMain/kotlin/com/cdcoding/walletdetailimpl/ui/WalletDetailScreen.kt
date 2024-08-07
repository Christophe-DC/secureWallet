package com.cdcoding.walletdetailimpl.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.cdcoding.core.designsystem.hooks.useInject
import com.cdcoding.core.designsystem.state.collectAsStateWithLifecycle
import com.cdcoding.model.AssetId
import com.cdcoding.model.TransactionExtended
import com.cdcoding.walletdetailimpl.presentation.AssetUIState
import com.cdcoding.walletdetailimpl.presentation.WalletDetailViewModel
import com.cdcoding.walletdetailimpl.presentation.WalletInfoUIState
import com.cdcoding.walletdetailimpl.ui.component.AssetList
import kotlinx.collections.immutable.ImmutableList

class WalletDetailScreen : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Filled.Add)

            return remember {
                TabOptions(
                    index = 0u,
                    title = "Wallet 1",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        //val viewModel: WalletDetailViewModel = useInject()
        // val viewModel = koinViewModel<WelcomeViewModel>()
        //val uiState = viewModel.state.collectAsStateWithLifecycle()


        /* val addNewPasswordScreen = rememberScreen(AddNewPasswordDestination.AddNewPasswordScreen)
         val accountScreen = rememberScreen(AccountDestination.Account)
         val authenticatorScreen = rememberScreen(AuthenticatorDestination.Authenticator)
         val passwordHealthScreen = rememberScreen(PasswordHealthDestination.PasswordHealth)
         val helpScreen = rememberScreen(HelpDestination.Help)
         val generatePasswordScreen = rememberScreen(GeneratePasswordDestination.GeneratePassword)
         val uiState = viewModel.state.collectAsStateWithLifecycle()*/

        // val createWalletScreen = rememberScreen(CreateWalletDestination.CreateWallet)
        //  val importWalletScreen = rememberScreen(ImportWalletDestination.ImportWallet)
        val viewModel: WalletDetailViewModel = useInject()
        val uiState by viewModel.state.collectAsStateWithLifecycle()

        /* Box(
             Modifier.width(300.dp)
                 .height(200.dp)
                 .background(color = Color.Green)
         ) {

             Text("version", color = Color.Red,
                 modifier = Modifier.background(color = Color.Blue).width(100.dp).height(200.dp)
             )
             //Text(uiState?.version.toString(), color = Color.Red)
         }*/
        WalletDetailScreenContent(
            isLoading = uiState.isLoading,
            walletInfo = uiState.walletInfo,
            assets = uiState.assets,
            transactions = uiState.pendingTransactions,
            swapEnabled = uiState.swapEnabled,
            onRefresh = viewModel::onRefresh,
            /*onShowWallets = onShowWallets,
            onShowAssetManage = onShowAssetManage,
            onSendClick = onSendClick,
            onReceiveClick = onReceiveClick,
            onBuyClick = onBuyClick,
            onSwapClick = onSwapClick,
            onTransactionClick = onTransactionClick,
            onAssetClick = onAssetClick,
            onAssetHide = viewModel::hideAsset,
            listState = listState*/
            //   navigateToCreateWallet = { navigator.push(createWalletScreen) },
            //     navigateToImportWallet = { navigator.push(importWalletScreen) },
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
    swapEnabled: Boolean,
    onRefresh: () -> Unit = {},
    onShowAssetManage: () -> Unit = {},
    onSendClick: () -> Unit = {},
    onReceiveClick: () -> Unit = {},
    onBuyClick: () -> Unit = {},
    onSwapClick: () -> Unit = {},
    onTransactionClick: (String) -> Unit = {},
    onAssetClick: (AssetId) -> Unit = {},
    onAssetHide: (AssetId) -> Unit = {},
    listState: LazyListState = rememberLazyListState()
) {
    val pullRefreshState = rememberPullRefreshState(isLoading, { onRefresh() })
    Box {
        AssetList(
            walletInfo = walletInfo,
            assets = assets,
            transactions = transactions,
            swapEnabled = swapEnabled,
            onShowAssetManage = onShowAssetManage,
            onSendClick = onSendClick,
            onReceiveClick = onReceiveClick,
            onBuyClick = onBuyClick,
            onSwapClick = onSwapClick,
            onTransactionClick = onTransactionClick,
            onAssetClick = onAssetClick,
            onAssetHide = onAssetHide,
            listState = listState,
        )
        PullRefreshIndicator(isLoading, pullRefreshState, Modifier.align(Alignment.TopCenter))
    }
}