package com.cdcoding.editwallet.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import com.cdcoding.core.designsystem.components.Scene
import com.cdcoding.core.designsystem.hooks.useInject
import com.cdcoding.core.designsystem.spacer.Spacer16
import com.cdcoding.core.designsystem.state.collectAsStateWithLifecycle
import com.cdcoding.core.designsystem.table.CellEntity
import com.cdcoding.core.designsystem.table.Table
import com.cdcoding.core.navigation.HomeDestination
import com.cdcoding.core.navigation.ShowPhraseDestination
import com.cdcoding.core.resource.Res
import com.cdcoding.core.resource.common_delete
import com.cdcoding.core.resource.common_show_secret_phrase
import com.cdcoding.core.resource.common_wallet
import com.cdcoding.core.resource.wallet_name
import com.cdcoding.editwallet.presentation.EditWalletIntent
import com.cdcoding.editwallet.presentation.EditWalletUIState
import com.cdcoding.editwallet.presentation.EditWalletViewModel
import org.jetbrains.compose.resources.stringResource


class EditWalletScreen(private val walletId: String) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val arguments = remember { mapOf("walletId" to walletId) }
        val viewModel: EditWalletViewModel = useInject(arguments = arguments)
        val uiState = viewModel.uiState.collectAsStateWithLifecycle()

        val showPhraseScreen = rememberScreen(ShowPhraseDestination.ShowPhrase(walletId))

        EditWalletScreenContent(
            uiState = uiState.value,
            onIntent = viewModel::setIntent,
            popBackStack = { navigator.pop() },
            onShowPhrase = { navigator.push(showPhraseScreen) },
        )
    }
}


@Composable
fun EditWalletScreenContent(
    uiState: EditWalletUIState,
    onIntent: (EditWalletIntent) -> Unit,
    popBackStack: () -> Unit,
    onShowPhrase: () -> Unit,
) {

    var walletName by remember(uiState.walletName) {
        mutableStateOf(uiState.walletName)
    }
    Scene(
        title = stringResource(Res.string.common_wallet),
        onClose = popBackStack
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                label = {
                    Text(text = stringResource(Res.string.wallet_name))
                },
                value = walletName,
                onValueChange = {
                    onIntent(EditWalletIntent.SetWalletName(it))
                    walletName = it
                },
                singleLine = true,
            )
            val actions = mutableListOf(
                CellEntity(
                    label = Res.string.common_show_secret_phrase,
                    data = "",
                    action = onShowPhrase
                )
            )

            HorizontalDivider(thickness = 0.4.dp)
            Table(items = actions)

            Spacer16()
            Button(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                colors = ButtonDefaults.buttonColors()
                    .copy(containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError),
                onClick = { onIntent(EditWalletIntent.OnDeleteWallet(popBackStack)) },
            ) {
                Text(text = stringResource(Res.string.common_delete))
            }
        }
    }
}
