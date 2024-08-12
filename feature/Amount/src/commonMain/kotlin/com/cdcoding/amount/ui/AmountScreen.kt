package com.cdcoding.amount.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.cdcoding.amount.presentation.AmountError
import com.cdcoding.amount.presentation.AmountIntent
import com.cdcoding.amount.presentation.AmountStateScreen
import com.cdcoding.amount.presentation.AmountUIState
import com.cdcoding.amount.presentation.AmountViewModel
import com.cdcoding.amount.ui.component.AssetInfoCard
import com.cdcoding.common.utils.getIconUrl
import com.cdcoding.core.designsystem.button.MainActionButton
import com.cdcoding.core.designsystem.components.FatalErrorView
import com.cdcoding.core.designsystem.hooks.useInject
import com.cdcoding.core.designsystem.state.collectAsStateWithLifecycle
import com.cdcoding.core.designsystem.textfield.AmountField
import com.cdcoding.core.navigation.HomeDestination
import com.cdcoding.core.resource.Res
import com.cdcoding.core.resource.amount_error_invalid_amount
import com.cdcoding.core.resource.common_continue
import com.cdcoding.core.resource.common_required_field
import com.cdcoding.core.resource.transfer_amount
import com.cdcoding.core.resource.transfer_amount_title
import com.cdcoding.core.resource.transfer_insufficient_balance
import com.cdcoding.core.resource.transfer_minimum_amount
import com.cdcoding.model.AssetId
import com.cdcoding.model.ConfirmParams
import com.cdcoding.model.TransactionType
import com.cdcoding.system.ui.theme.largeMarginDimens
import org.jetbrains.compose.resources.stringResource


class AmountScreen(
    private val assetId: AssetId,
    private val destinationAddress: String,
    private val addressDomain: String,
    private val memo: String,
    private val txType: TransactionType = TransactionType.Transfer,
) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val arguments = remember {
            mapOf(
                "assetId" to assetId,
                "destinationAddress" to destinationAddress,
                "addressDomain" to addressDomain,
                "memo" to memo,
                "txType" to txType
            )
        }
        val viewModel: AmountViewModel = useInject(arguments = arguments)
        val uiState = viewModel.uiState.collectAsStateWithLifecycle()


        val HomeDestination = rememberScreen(HomeDestination.Home)

        AmountScreenContent(
            uiState = uiState.value,
            onIntent = viewModel::setIntent,
            popBackStack = { navigator.pop() },
            nextStack = { navigator.push(HomeDestination) }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmountScreenContent(
    modifier: Modifier = Modifier,
    uiState: AmountUIState,
    onIntent: (AmountIntent) -> Unit,
    nextStack: (confirmParams: ConfirmParams) -> Unit,
    popBackStack: () -> Unit,
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
                            text = stringResource(Res.string.transfer_amount_title),
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(largeMarginDimens.margin),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            when (uiState.screen) {
                is AmountStateScreen.Fatal -> FatalErrorView(
                    message = uiState.screen.error,
                )

                is AmountStateScreen.Loaded -> {
                    AmountLoaded(
                        uiState = uiState,
                        onIntent = onIntent,
                        nextStack = nextStack,
                    )
                }


                is AmountStateScreen.Loading -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }
            }
        }

    }
}


@Composable
private fun AmountLoaded(
    uiState: AmountUIState,
    onIntent: (AmountIntent) -> Unit,
    nextStack: (confirmParams: ConfirmParams) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            Spacer(modifier = Modifier.size(40.dp))
            AmountField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                amount = uiState.amount,
                assetSymbol = uiState.assetInfo?.asset?.symbol ?: "",
                equivalent = uiState.equivalent,
                error = amountErrorString(error = uiState.error),
                onValueChange = { input ->
                    onIntent(
                        AmountIntent.OnUpdateAmount(input)
                    )
                },
                onNext = {
                    onIntent(
                        AmountIntent.OnNext(nextStack)
                    )
                }
            )
            if (uiState.assetInfo != null) {
                AssetInfoCard(
                    assetId = uiState.assetInfo.asset.id,
                    assetIcon = uiState.assetInfo.asset.getIconUrl(),
                    assetTitle = uiState.assetInfo.asset.name,
                    assetType = uiState.assetInfo.asset.type,
                    availableAmount = uiState.availableAmount,
                    onMaxAmount = { onIntent(AmountIntent.OnMaxAmount) }
                )
            }

        }
    }


    MainActionButton(
        title = stringResource(Res.string.common_continue),
        enabled = uiState.error == AmountError.None,
        loading = uiState.loading,
        onClick = {
            if (uiState.assetInfo?.asset?.address != null) {
                onIntent(AmountIntent.OnNext(nextStack))
            }
        }
    )

}


@Composable
fun amountErrorString(error: AmountError): String = when (error) {
    AmountError.None -> ""
    AmountError.IncorrectAmount -> stringResource(Res.string.amount_error_invalid_amount)
    AmountError.Init -> "Init error"
    AmountError.Required -> stringResource(
        Res.string.common_required_field,
        stringResource(Res.string.transfer_amount)
    )

    AmountError.Unavailable -> ""
    is AmountError.InsufficientBalance -> stringResource(
        Res.string.transfer_insufficient_balance,
        error.assetName
    )

    AmountError.ZeroAmount -> ""
    is AmountError.MinimumValue -> stringResource(
        Res.string.transfer_minimum_amount,
        error.minimumValue
    )
}
