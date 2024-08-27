package com.cdcoding.welcome.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.cdcoding.core.designsystem.hooks.useEffect
import com.cdcoding.core.designsystem.hooks.useInject
import com.cdcoding.core.navigation.CreateWalletDestination
import com.cdcoding.core.navigation.HomeDestination
import com.cdcoding.core.navigation.ImportWalletDestination
import com.cdcoding.core.resource.Res
import com.cdcoding.core.resource.create_wallet
import com.cdcoding.core.resource.import_wallet
import com.cdcoding.core.resource.secure_wallet_logo
import com.cdcoding.core.resource.welcome_msg
import com.cdcoding.system.ui.theme.largeMarginDimens
import com.cdcoding.system.ui.theme.mediumMarginDimens
import com.cdcoding.welcome.presentation.WelcomeViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

class WelcomeScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val viewModel: WelcomeViewModel = useInject()
        val createWalletScreen = rememberScreen(CreateWalletDestination.CreateWallet)
        val importWalletScreen = rememberScreen(ImportWalletDestination.ImportWallet)

        val homeScreen = rememberScreen(HomeDestination.Home)

        useEffect(viewModel.uiState.value.hasSession) {
            if (viewModel.uiState.value.hasSession) {
                navigator.replace(homeScreen)
            }
        }

        WelcomeScreenContent(
            navigateToCreateWallet = { navigator.push(createWalletScreen) },
            navigateToImportWallet = { navigator.push(importWalletScreen) },
        )
    }
}


@Composable
fun WelcomeScreenContent(
    modifier: Modifier = Modifier,
    navigateToCreateWallet: () -> Unit = {},
    navigateToImportWallet: () -> Unit = {}
) {

    Column(
        modifier.fillMaxWidth()
            .padding(largeMarginDimens.margin),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(largeMarginDimens.margin))
        Text(
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground,
            text = stringResource(Res.string.welcome_msg)
        )
        Spacer(Modifier.height(largeMarginDimens.margin))
        Image(painterResource(Res.drawable.secure_wallet_logo), null)
        Spacer(Modifier.height(48.dp))
        Card(
            Modifier
                .fillMaxWidth()
                .clickable { navigateToCreateWallet.invoke() },

            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            )
        ) {
            Row(
                Modifier.padding(largeMarginDimens.margin),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilledTonalIconButton(
                    onClick = {},
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f),
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Create wallet")
                }
                Spacer(Modifier.width(mediumMarginDimens.margin))
                Text(stringResource(Res.string.create_wallet))
            }
        }
        Spacer(Modifier.height(largeMarginDimens.margin))
        Card(
            Modifier
                .fillMaxWidth()
                .clickable { navigateToImportWallet() },
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            )
        ) {
            Row(
                Modifier.padding(largeMarginDimens.margin),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilledTonalIconButton(
                    onClick = {},
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f),
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Icon(Icons.Filled.ArrowDownward, contentDescription = "Import wallet")
                }
                Spacer(Modifier.width(mediumMarginDimens.margin))
                Text(stringResource(Res.string.import_wallet))
            }
        }
    }
}
