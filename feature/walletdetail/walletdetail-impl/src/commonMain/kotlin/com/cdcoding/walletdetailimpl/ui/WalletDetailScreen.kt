package com.cdcoding.walletdetailimpl.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.cdcoding.system.ui.theme.largeMarginDimens
import com.cdcoding.core.resource.Res
import com.cdcoding.core.resource.welcome_msg
import org.jetbrains.compose.resources.stringResource

class WalletDetailScreen : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Filled.Add)

            return remember {
                TabOptions(
                    index = 0u,
                    title = "Wallet",
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

        WelcomeScreenContent(
            //   navigateToCreateWallet = { navigator.push(createWalletScreen) },
            //     navigateToImportWallet = { navigator.push(importWalletScreen) },
        )
    }
}


@Composable
fun WelcomeScreenContent(modifier: Modifier = Modifier) {

    Column(
        Modifier.fillMaxWidth()
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
    }
}
