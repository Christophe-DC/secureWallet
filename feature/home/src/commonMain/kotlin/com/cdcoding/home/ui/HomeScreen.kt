package com.cdcoding.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
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
import com.cdcoding.core.designsystem.hooks.useEffect
import com.cdcoding.core.designsystem.hooks.useInject
import com.cdcoding.core.designsystem.state.collectAsStateWithLifecycle
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

        useEffect(true) {
            if (!uiState.value.hasSession) {
                navigator.replace(welcomeScreen)
            }
        }

        HomeScreenContent()
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalVoyagerApi::class)
@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier
) {

    val walletDetailTab = rememberTab(WalletDetailDestination.WalletDetail)

    TabNavigator(
        walletDetailTab,
        tabDisposable = {
            TabDisposable(
                navigator = it,
                tabs = listOf(walletDetailTab)
            )
        }
    ) { tabNavigator ->
        Scaffold(
            modifier = modifier,
            containerColor = MaterialTheme.colorScheme.surface,
            topBar = {
                TopAppBar(
                    title = {
                        if (tabNavigator.current.key == walletDetailTab.key) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = tabNavigator.current.options.title,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Icon(
                                    imageVector = Icons.Filled.ExpandMore,
                                    contentDescription = null
                                )
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
                )
            },
            content = { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    CurrentTab()
                }
            },
            bottomBar = {
                BottomNavigation {
                    TabNavigationItem(walletDetailTab)
                }
            }
        )
    }
}

@Composable
private fun RowScope.TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current

    BottomNavigationItem(
        selected = tabNavigator.current.key == tab.key,
        onClick = { tabNavigator.current = tab },
        icon = { Icon(painter = tab.options.icon!!, contentDescription = tab.options.title) }
    )
}
