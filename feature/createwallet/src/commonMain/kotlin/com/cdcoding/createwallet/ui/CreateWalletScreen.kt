package com.cdcoding.createwallet.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.cdcoding.core.designsystem.circularProgress.CircularProgressIndicator32
import com.cdcoding.core.designsystem.components.Scene
import com.cdcoding.core.designsystem.hooks.useEffect
import com.cdcoding.core.designsystem.hooks.useInject
import com.cdcoding.core.designsystem.hooks.useScope
import com.cdcoding.core.designsystem.hooks.useSnackbar
import com.cdcoding.core.designsystem.state.collectAsStateWithLifecycle
import com.cdcoding.core.designsystem.textfield.SWTextField
import com.cdcoding.core.navigation.HomeDestination
import com.cdcoding.core.resource.Res
import com.cdcoding.core.resource.create
import com.cdcoding.core.resource.create_wallet
import com.cdcoding.core.resource.name
import com.cdcoding.core.resource.wallet_created
import com.cdcoding.createwallet.presentation.CreateWalletEffect
import com.cdcoding.createwallet.presentation.CreateWalletIntent
import com.cdcoding.createwallet.presentation.CreateWalletState
import com.cdcoding.createwallet.presentation.CreateWalletViewModel
import com.cdcoding.system.ui.theme.largeMarginDimens
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource

class CreateWalletScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val viewModel: CreateWalletViewModel = useInject()
        val uiState = viewModel.uiState.collectAsStateWithLifecycle()

        val homeScreenWithWalletCreatedEvent = rememberScreen(HomeDestination.Home)

        CreateWalletScreenContent(
            uiState = uiState.value,
            onEvent = viewModel::setIntent,
            eventFlow = viewModel.effect,
            popBackStack = { navigator.pop() },
            onWalletCreated = {
                navigator.replaceAll(homeScreenWithWalletCreatedEvent)
            }
        )
    }
}


@Composable
fun CreateWalletScreenContent(
    uiState: CreateWalletState,
    onEvent: (CreateWalletIntent) -> Unit,
    eventFlow: Flow<CreateWalletEffect>,
    popBackStack: () -> Unit,
    onWalletCreated: () -> Unit,
) {
    val snackbarState = useSnackbar()
    val scope = useScope()
    useEffect(true) {
        eventFlow.collectLatest { newEffect ->
            when (newEffect) {
                is CreateWalletEffect.Failure -> {
                    scope.launch { snackbarState.showSnackbar(newEffect.message) }
                }

                CreateWalletEffect.WalletCreated -> {
                    scope.launch { snackbarState.showSnackbar(getString(Res.string.wallet_created)) }
                    onWalletCreated.invoke()
                }

            }
        }
    }

    Scene(
        title = stringResource(Res.string.create_wallet),
        onClose = popBackStack,
        snackbar = snackbarState,
        mainAction = {
            if (uiState.walletIsCreating) {
                CircularProgressIndicator32(modifier = Modifier.padding(bottom = largeMarginDimens.margin))
            } else {
                FilledTonalButton(
                    modifier = Modifier.padding(bottom = largeMarginDimens.margin),
                    onClick = { onEvent(CreateWalletIntent.OnCreateNewWallet) },
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f),
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    enabled = uiState.walletName.isNotEmpty()
                ) {
                    Text(
                        text = stringResource(Res.string.create),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    ) {
        SWTextField(
            label = stringResource(Res.string.name),
            hint = uiState.defaultWalletName,
            textValue = uiState.walletName,
            onValueChanged = { newText -> onEvent(CreateWalletIntent.OnWalletNameChanged(newText)) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

