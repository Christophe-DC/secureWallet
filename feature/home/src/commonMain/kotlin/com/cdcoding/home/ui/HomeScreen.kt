package com.cdcoding.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabDisposable
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.cdcoding.core.designsystem.components.Scene
import com.cdcoding.core.designsystem.hooks.useEffect
import com.cdcoding.core.designsystem.hooks.useInject
import com.cdcoding.core.designsystem.state.collectAsStateWithLifecycle
import com.cdcoding.core.navigation.SelectWalletDestination
import com.cdcoding.core.navigation.TransactionsDestination
import com.cdcoding.core.navigation.WelcomeDestination
import com.cdcoding.core.navigation.tab.WalletDetailDestination
import com.cdcoding.core.navigation.tab.registry.rememberTab
import com.cdcoding.home.presentation.HomeViewModel

class HomeScreen : Screen {

    @Composable
    override fun Content() {

        val viewModel: HomeViewModel = useInject()
        val uiState = viewModel.uiState.collectAsStateWithLifecycle()
        val navigator = LocalNavigator.currentOrThrow

        val welcomeScreen = rememberScreen(WelcomeDestination.Welcome)
        val selectWalletScreen = rememberScreen(SelectWalletDestination.SelectWallet)

        useEffect(uiState.value.hasSession) {
            if (!uiState.value.hasSession) {
                navigator.replace(welcomeScreen)
            }
        }

        HomeScreenContent(
            onSelectWallet = { navigator.push(selectWalletScreen) }
        )
    }
}


@OptIn(ExperimentalVoyagerApi::class)
@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    onSelectWallet: () -> Unit,
) {

    val walletDetailTab = rememberTab(WalletDetailDestination.WalletDetail)
    val transactionsTab = rememberTab(TransactionsDestination.Transactions)

    TabNavigator(
        walletDetailTab,
        tabDisposable = {
            TabDisposable(
                navigator = it,
                tabs = listOf(walletDetailTab)
            )
        }
    ) { tabNavigator ->
        Scene(
            title = {
                if (tabNavigator.current.key == walletDetailTab.key) {
                    Box {
                        TextButton(onClick = onSelectWallet) {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = tabNavigator.current.options.title,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Icon(
                                    imageVector = Icons.Filled.ExpandMore,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = tabNavigator.current.options.title,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            },
            mainActionPadding = PaddingValues(0.dp),
            mainAction = {
                BottomNavigation(
                    backgroundColor = MaterialTheme.colorScheme.surfaceContainerLow,
                ) {
                    TabNavigationItem(walletDetailTab)
                    TabNavigationItem(transactionsTab)
                }
            }
        ) {
            CurrentTab()
        }
    }
}

@Composable
private fun RowScope.TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current

    BottomNavigationItem(
        selected = tabNavigator.current.key == tab.key,
        onClick = { tabNavigator.current = tab },
        icon = {
            Icon(
                modifier = Modifier.alpha(if (tabNavigator.current.key == tab.key) 1f else 0.5f),
                painter = tab.options.icon!!,
                contentDescription = tab.options.title
            )
        },
        enabled = tabNavigator.current.key != tab.key,
    )
}
