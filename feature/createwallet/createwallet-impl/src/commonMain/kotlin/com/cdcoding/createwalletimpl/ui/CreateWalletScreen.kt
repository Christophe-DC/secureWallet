package com.cdcoding.createwalletimpl.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.cdcoding.core.designsystem.circularProgress.CircularProgressIndicator16
import com.cdcoding.core.designsystem.circularProgress.CircularProgressIndicator32
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
import com.cdcoding.createwalletimpl.presentation.CreateWalletEffect
import com.cdcoding.createwalletimpl.presentation.CreateWalletEvent
import com.cdcoding.createwalletimpl.presentation.CreateWalletState
import com.cdcoding.system.ui.theme.largeMarginDimens
import com.cdcoding.createwalletimpl.presentation.CreateWalletViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getResourceUri
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource

class CreateWalletScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val viewModel: CreateWalletViewModel = useInject()
        val uiState = viewModel.state.collectAsStateWithLifecycle()


        /* val addNewPasswordScreen = rememberScreen(AddNewPasswordDestination.AddNewPasswordScreen)
         val accountScreen = rememberScreen(AccountDestination.Account)
         val authenticatorScreen = rememberScreen(AuthenticatorDestination.Authenticator)
         val passwordHealthScreen = rememberScreen(PasswordHealthDestination.PasswordHealth)
         val helpScreen = rememberScreen(HelpDestination.Help)
         val generatePasswordScreen = rememberScreen(GeneratePasswordDestination.GeneratePassword)
         val uiState = viewModel.state.collectAsStateWithLifecycle()*/

        // val createWalletScreen = rememberScreen(CreateWalletDestination.CreateWallet)
        //  val importWalletScreen = rememberScreen(ImportWalletDestination.ImportWallet)

        val homeScreenWithWalletCreatedEvent = rememberScreen(HomeDestination.Home)

        CreateWalletScreenContent(
            uiState = uiState.value,
            onEvent = viewModel::onEvent,
            eventFlow = viewModel.eventFlow,
            popBackStack = { navigator.pop() },
            onWalletCreated = {
                navigator.replaceAll(homeScreenWithWalletCreatedEvent)
            }
            //     navigateToImportWallet = { navigator.push(importWalletScreen) },
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateWalletScreenContent(
    modifier: Modifier = Modifier,
    uiState: CreateWalletState,
    onEvent: (CreateWalletEvent) -> Unit,
    eventFlow: SharedFlow<CreateWalletEffect>,
    popBackStack: () -> Unit,
    onWalletCreated: () -> Unit,
) {

    /*useEffect(true) {
        flow.collectLatest { effect ->
            when (effect) {
                is AddNewPasswordEffect.Failure -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = effect.message,
                        )
                    }
                }
                AddNewPasswordEffect.GeneratePassword -> {
                    navigateToGeneratePassword.invoke()
                }
                is AddNewPasswordEffect.Success -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = effect.message,
                        )
                    }
                }
            }
        }
    }*/

    val snackbarState = useSnackbar()
    val scope = useScope()
    //var text by remember { mutableStateOf("") }

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


    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarState,
                modifier = modifier.fillMaxWidth().wrapContentHeight(Alignment.Bottom),
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.create_wallet),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.fillMaxWidth(),
                    )
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
            SWTextField(
                label = stringResource(Res.string.name),
                hint = uiState.defaultWalletName,
                textValue = uiState.walletName,
                onValueChanged = { newText -> onEvent(CreateWalletEvent.OnWalletNameChanged(newText)) },
                modifier = Modifier.fillMaxWidth()
            )

            if (uiState.walletIsCreating) {
                CircularProgressIndicator32(modifier = Modifier.padding(bottom = largeMarginDimens.margin))
            } else {
                FilledTonalButton(
                    modifier = Modifier.padding(bottom = largeMarginDimens.margin),
                    onClick = { onEvent(CreateWalletEvent.OnCreateNewWallet) },
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
    }

}
