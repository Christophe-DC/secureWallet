package com.cdcoding.confirm.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.cdcoding.confirm.presentation.ConfirmError
import com.cdcoding.confirm.presentation.ConfirmIntent
import com.cdcoding.confirm.presentation.ConfirmStateScreen
import com.cdcoding.confirm.presentation.ConfirmUIState
import com.cdcoding.confirm.presentation.ConfirmViewModel
import com.cdcoding.confirm.ui.component.SwapListHead
import com.cdcoding.core.designsystem.button.MainActionButton
import com.cdcoding.core.designsystem.components.AmountListHead
import com.cdcoding.core.designsystem.components.FatalErrorView
import com.cdcoding.core.designsystem.components.Scene
import com.cdcoding.core.designsystem.hooks.useInject
import com.cdcoding.core.designsystem.state.collectAsStateWithLifecycle
import com.cdcoding.core.designsystem.table.Table
import com.cdcoding.core.designsystem.util.getTitle
import com.cdcoding.model.ConfirmParams
import com.cdcoding.model.Crypto
import com.cdcoding.model.TransactionType


class ConfirmScreen(
    private val confirmParams: ConfirmParams,
    private val txType: TransactionType = TransactionType.Transfer,
) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val arguments = remember {
            mapOf(
                "confirmParams" to confirmParams,
                "txType" to txType
            )
        }
        val viewModel: ConfirmViewModel = useInject(arguments = arguments)
        val uiState = viewModel.uiState.collectAsStateWithLifecycle()


        ConfirmScreenContent(
            uiState = uiState.value,
            onIntent = viewModel::setIntent,
            popBackStack = { navigator.pop() },
            onFinish = { navigator.popUntilRoot() }
        )
    }
}


@Composable
fun ConfirmScreenContent(
    uiState: ConfirmUIState,
    onIntent: (ConfirmIntent) -> Unit,
    onFinish: (String) -> Unit,
    popBackStack: () -> Unit,
) {

    Scene(
        title = uiState.signerParams?.input?.getTxType()?.getTitle() ?: "",
        onClose = popBackStack,
        mainAction = {
            MainActionButton(
                title = uiState.error.toStringResource(),
                enabled = uiState.error == ConfirmError.None,
                loading = uiState.sending,
                onClick = { onIntent(ConfirmIntent.OnSend) }
            )
        }
    ) {

        when (uiState.screen) {
            is ConfirmStateScreen.Fatal -> FatalErrorView(
                message = uiState.screen.error,
            )

            is ConfirmStateScreen.Loaded -> {
                ConfirmScreenLoaded(
                    uiState = uiState,
                    onFinish = onFinish,
                )
            }

            is ConfirmStateScreen.Loading -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
        }



    }
}


@Composable
fun ConfirmScreenLoaded(
    uiState: ConfirmUIState,
    onFinish: (String) -> Unit,
) {
    if (uiState.txType == TransactionType.Swap &&
        uiState.assetInfo != null &&
        uiState.signerParams != null &&
        uiState.toAssetInfo != null
    ) {
        SwapListHead(
            fromAsset = uiState.assetInfo,
            fromValue = uiState.signerParams.finalAmount.toString(),
            toAsset = uiState.toAssetInfo,
            toValue = uiState.toAmount.toString(),
            currency = uiState.currency,
        )
    } else {
        if (uiState.assetInfo != null && uiState.signerParams != null) {
            val decimals = uiState.assetInfo.asset.decimals
            val symbol = uiState.assetInfo.asset.symbol
            val price = uiState.assetInfo.price?.price?.price ?: 0.0
            val amount = Crypto(uiState.signerParams.finalAmount)
            AmountListHead(
                amount = amount.format(decimals, symbol, -1),
                equivalent = amount.convert(decimals, price).format(0, uiState.currency.string, 2),
            )
        }
    }
    Table(uiState.cells)
    if (uiState.txHash.isNotEmpty()) {
        onFinish(uiState.txHash)
    }
}
