package com.cdcoding.selectasset.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.text2.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.cdcoding.common.utils.asset
import com.cdcoding.common.utils.toIdentifier
import com.cdcoding.common.utils.type
import com.cdcoding.core.designsystem.circularProgress.CircularProgressIndicator16
import com.cdcoding.core.designsystem.components.AssetListItem
import com.cdcoding.core.designsystem.components.getBalanceInfo
import com.cdcoding.core.designsystem.hooks.useEffect
import com.cdcoding.core.designsystem.hooks.useInject
import com.cdcoding.core.designsystem.hooks.useScope
import com.cdcoding.core.designsystem.hooks.useSnackbar
import com.cdcoding.core.designsystem.spacer.Spacer16
import com.cdcoding.core.designsystem.state.collectAsStateWithLifecycle
import com.cdcoding.core.designsystem.textfield.SearchBar
import com.cdcoding.core.navigation.SendAssetDestination
import com.cdcoding.core.resource.Res
import com.cdcoding.core.resource.select_asset_send_title
import com.cdcoding.core.resource.assets_no_assets_found
import com.cdcoding.core.resource.assets_add_custom_token
import com.cdcoding.core.resource.wallet_created
import com.cdcoding.model.AssetId
import com.cdcoding.model.AssetSubtype
import com.cdcoding.model.AssetUIState
import com.cdcoding.selectasset.presentation.SelectAssetEvent
import com.cdcoding.selectasset.presentation.SelectAssetIntent
import com.cdcoding.selectasset.presentation.SelectAssetState
import com.cdcoding.selectasset.presentation.SelectAssetViewModel
import com.cdcoding.system.ui.theme.largeMarginDimens
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource


class SelectAssetScreen : Screen {

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val viewModel: SelectAssetViewModel = useInject()
        val uiState = viewModel.uiState.collectAsStateWithLifecycle()
        HandleEvents(viewModel.effect)


        //val homeScreenWithWalletCreatedEvent = rememberScreen(HomeDestination.Home)

        SelectAssetScreenContent(
            uiState = uiState.value,
            query = viewModel.query,
            onIntent = viewModel::setIntent,
            //onEvent = viewModel::onEvent,
            //eventFlow = viewModel.eventFlow,
            popBackStack = { navigator.pop() },
            onSelect = { AssetId ->
                val sendAssetScreen = ScreenRegistry.get(SendAssetDestination.SendAsset(AssetId))
                navigator.push(sendAssetScreen)
            },
            support = { if (it.id.type() == AssetSubtype.NATIVE) null else it.id.chain.asset().name },

            /* onWalletCreated = {
                 navigator.replaceAll(homeScreenWithWalletCreatedEvent)
             }*/
            //     navigateToImportWallet = { navigator.push(importWalletScreen) },
        )
    }
}


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SelectAssetScreenContent(
    modifier: Modifier = Modifier,
    uiState: SelectAssetState,
    onIntent: (SelectAssetIntent) -> Unit,
    query: TextFieldState,
    //onEvent: (CreateWalletEvent) -> Unit,
    //eventFlow: SharedFlow<CreateWalletEffect>,
    popBackStack: () -> Unit,
    onSelect: ((AssetId) -> Unit)? = {},
    support: ((AssetUIState) -> String?)?,
    onAddAsset: (() -> Unit)? = null,
) {


    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(Res.string.select_asset_send_title),
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = popBackStack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBackIosNew,
                            contentDescription = "Back"
                        )
                    }
                },
            )
        }
    ) { padding ->

        val items by remember(uiState.assets) { mutableStateOf(uiState.assets) }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(largeMarginDimens.margin),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SearchBar(
                modifier = Modifier.padding(horizontal = 16.dp),
                query = query,
            )
            Spacer16()
            LazyColumn {
                assets(items, onSelect, support)
                loading(loading = uiState.isLoading)
                notFound(items = items, loading = uiState.isLoading, onAddAsset = onAddAsset)
            }

        }
    }
}

private fun LazyListScope.assets(
    items: List<AssetUIState>,
    onSelect: ((AssetId) -> Unit)?,
    support: ((AssetUIState) -> String?)?,
) {
    if (items.isEmpty()) {
        return
    }
    items(items.size, key = { items[it].id.toIdentifier() }) { index ->
        val asset = items[index]
        AssetListItem(
            modifier = Modifier
                .heightIn(74.dp)
                .clickable { onSelect?.invoke(asset.id) },
            chain = asset.id.chain,
            title = asset.name,
            support = support?.invoke(asset),
            assetType = asset.type,
            iconUrl = asset.icon,
            badge = null,
            trailing = { getBalanceInfo(asset.isZeroValue, asset.value, asset.fiat) },
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun LazyListScope.notFound(
    loading: Boolean,
    items: List<AssetUIState>,
    onAddAsset: (() -> Unit)? = null,
) {
    if (items.isNotEmpty() || loading) {
        return
    }
    item {
        Box(
            modifier = Modifier
                .animateItemPlacement()
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.align(Alignment.Center)) {
                Text(text = stringResource(Res.string.assets_no_assets_found))
                TextButton(onClick = { onAddAsset?.invoke() }) {
                    Text(text = stringResource(Res.string.assets_add_custom_token))
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun LazyListScope.loading(loading: Boolean) {
    if (!loading) {
        return
    }
    item {
        Box(modifier = Modifier.animateItemPlacement().fillMaxWidth().padding(16.dp)) {
            CircularProgressIndicator16(Modifier.align(Alignment.Center))
        }
    }
}

@Composable
fun HandleEvents(
    events: Flow<SelectAssetEvent>
) {
    val snackbarState = useSnackbar()
    val scope = useScope()
    useEffect(true) {
        events.collectLatest { newEffect ->
            when (newEffect) {
                is SelectAssetEvent.ShowToast -> {
                    scope.launch { snackbarState.showSnackbar(newEffect.message) }
                }
            }
        }
    }
}