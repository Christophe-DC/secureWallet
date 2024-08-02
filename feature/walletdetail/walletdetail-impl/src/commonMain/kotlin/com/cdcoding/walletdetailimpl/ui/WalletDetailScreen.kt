package com.cdcoding.walletdetailimpl.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.cdcoding.common.utils.getIconUrl
import com.cdcoding.common.utils.getSupportIconUrl
import com.cdcoding.common.utils.toIdentifier
import com.cdcoding.core.designsystem.hooks.useInject
import com.cdcoding.core.designsystem.state.collectAsStateWithLifecycle
import com.cdcoding.system.ui.theme.largeMarginDimens
import com.cdcoding.core.resource.Res
import com.cdcoding.core.resource.welcome_msg
import com.cdcoding.model.Asset
import com.cdcoding.model.AssetId
import com.cdcoding.model.AssetType
import com.cdcoding.model.Chain
import com.cdcoding.model.Crypto
import com.cdcoding.model.Transaction
import com.cdcoding.model.TransactionDirection
import com.cdcoding.model.TransactionExtended
import com.cdcoding.model.TransactionState
import com.cdcoding.model.TransactionSwapMetadata
import com.cdcoding.model.TransactionType
import com.cdcoding.model.WalletType
import com.cdcoding.network.model.FiatAssets
import com.cdcoding.walletdetailimpl.presentation.AssetUIState
import com.cdcoding.walletdetailimpl.presentation.PriceState
import com.cdcoding.walletdetailimpl.presentation.PriceUIState
import com.cdcoding.walletdetailimpl.presentation.WalletDetailUIState
import com.cdcoding.walletdetailimpl.presentation.WalletDetailViewModel
import com.cdcoding.walletdetailimpl.presentation.WalletInfoUIState
import com.cdcoding.walletdetailimpl.ui.component.AssetList
import com.cdcoding.walletdetailimpl.ui.component.PriceInfo
import io.kamel.image.asyncPainterResource
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.stringResource

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