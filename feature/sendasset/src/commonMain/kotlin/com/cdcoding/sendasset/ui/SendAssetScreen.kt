package com.cdcoding.sendasset.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.cdcoding.core.designsystem.button.MainActionButton
import com.cdcoding.core.designsystem.components.AddressChainField
import com.cdcoding.core.designsystem.components.FatalErrorView
import com.cdcoding.core.designsystem.components.QrCodeRequest
import com.cdcoding.core.designsystem.components.Scene
import com.cdcoding.core.designsystem.hooks.useEffect
import com.cdcoding.core.designsystem.hooks.useInject
import com.cdcoding.core.designsystem.hooks.useScope
import com.cdcoding.core.designsystem.hooks.useSnackbar
import com.cdcoding.core.designsystem.state.collectAsStateWithLifecycle
import com.cdcoding.core.navigation.AmountDestination
import com.cdcoding.core.resource.Res
import com.cdcoding.core.resource.common_continue
import com.cdcoding.core.resource.errors_invalid_address_name
import com.cdcoding.core.resource.transaction_recipient
import com.cdcoding.core.resource.transfer_recipient_address_field
import com.cdcoding.model.AssetId
import com.cdcoding.model.NameRecord
import com.cdcoding.sendasset.presentation.RecipientFormError
import com.cdcoding.sendasset.presentation.ScanType
import com.cdcoding.sendasset.presentation.SendAssetEvent
import com.cdcoding.sendasset.presentation.SendAssetIntent
import com.cdcoding.sendasset.presentation.SendAssetStateScreen
import com.cdcoding.sendasset.presentation.SendAssetUIState
import com.cdcoding.sendasset.presentation.SendAssetViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource


class SendAssetScreen(
    private val assetId: AssetId,
    private val destinationAddress: String? = null,
    private val addressDomain: String? = null,
    private val memo: String? = null
) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val arguments = remember {
            mapOf(
                "assetId" to assetId,
                "destinationAddress" to destinationAddress,
                "addressDomain" to addressDomain,
                "memo" to memo
            )
        }
        val viewModel: SendAssetViewModel = useInject(arguments = arguments)
        val uiState = viewModel.uiState.collectAsStateWithLifecycle()
        HandleEvents(viewModel.effect)


        val amountScreen = rememberScreen(
            AmountDestination.Amount(
                uiState.value.assetInfo?.asset?.id ?: assetId,
                uiState.value.address,
                uiState.value.addressDomain,
                uiState.value.memo
            )
        )

        SendAssetScreenContent(
            uiState = uiState.value,
            onIntent = viewModel::setIntent,
            popBackStack = { navigator.pop() },
            nextStack = { navigator.push(amountScreen) }
        )
    }
}


@Composable
fun SendAssetScreenContent(
    uiState: SendAssetUIState,
    onIntent: (SendAssetIntent) -> Unit,
    nextStack: () -> Unit,
    popBackStack: () -> Unit,
) {

    var inputStateError by remember(uiState.address, uiState.addressError) {
        mutableStateOf(uiState.addressError)
    }
    var nameRecordState by remember(uiState.addressDomain) {
        mutableStateOf<NameRecord?>(null)
    }
    Scene(
        title = stringResource(Res.string.transaction_recipient),
        onClose = {
            if (uiState.screen is SendAssetStateScreen.ScanQr) {
                onIntent(SendAssetIntent.OnScanCanceled)
            } else {
                popBackStack()
            }
        },
        mainAction = {
            if (uiState.screen == SendAssetStateScreen.Idle) {
                MainActionButton(
                    title = stringResource(Res.string.common_continue),
                    enabled = inputStateError == RecipientFormError.None,
                    onClick = {
                        onIntent(
                            SendAssetIntent.OnNext(
                                uiState.address,
                                nameRecordState,
                                uiState.memo,
                                nextStack
                            )
                        )
                    },
                )
            }
        }
    ) {
        when (uiState.screen) {
            is SendAssetStateScreen.Fatal -> FatalErrorView(
                message = uiState.screen.error,
            )

            is SendAssetStateScreen.Idle -> {
                val assetInfo = uiState.assetInfo
                if (assetInfo != null) {
                    AddressChainField(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        chain = assetInfo.asset.id.chain,
                        value = uiState.address,
                        label = stringResource(Res.string.transfer_recipient_address_field),
                        error = recipientErrorString(error = inputStateError),
                        onValueChange = { input, nameRecord ->
                            onIntent(SendAssetIntent.OnValueChange(input, nameRecord))
                            nameRecordState = nameRecord
                            inputStateError = RecipientFormError.None
                        },
                        onQrScanner = { onIntent(SendAssetIntent.OnQrScanner(ScanType.Address)) }
                    )
                }
            }

            is SendAssetStateScreen.Loading -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }

            is SendAssetStateScreen.ScanQr -> QrCodeRequest(
                onResult = { onIntent(SendAssetIntent.SetQrData(it)) },
                onCanceled = { onIntent(SendAssetIntent.OnScanCanceled) }
            )
        }
    }
}

@Composable
private fun recipientErrorString(error: RecipientFormError): String {
    return when (error) {
        RecipientFormError.IncorrectAddress -> stringResource(Res.string.errors_invalid_address_name)
        RecipientFormError.None -> ""
    }
}


@Composable
fun HandleEvents(
    events: Flow<SendAssetEvent>
) {
    val snackbarState = useSnackbar()
    val scope = useScope()
    useEffect(true) {
        events.collectLatest { newEffect ->
            when (newEffect) {
                is SendAssetEvent.ShowToast -> {
                    scope.launch { snackbarState.showSnackbar(newEffect.message) }
                }
            }
        }
    }
}