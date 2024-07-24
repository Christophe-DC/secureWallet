package com.cdcoding.homeimpl.ui

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.cdcoding.core.designsystem.hooks.useScope
import com.cdcoding.core.designsystem.hooks.useSnackbar
import com.cdcoding.core.designsystem.state.collectAsStateWithLifecycle
import com.cdcoding.core.navigation.HomeDestination
import com.cdcoding.core.navigation.HomeDestinationEvent
import com.cdcoding.core.navigation.WelcomeDestination
import com.cdcoding.core.navigation.tab.WalletDetailDestination
import com.cdcoding.core.navigation.tab.registry.rememberTab
import com.cdcoding.homeimpl.presentation.HomeViewModel
import kotlinx.coroutines.launch

class HomeScreen(private val homeDestinationEvent: HomeDestinationEvent) : Screen {

    @Composable
    override fun Content() {

        val viewModel: HomeViewModel = useInject()
        val uiState = viewModel.state.collectAsStateWithLifecycle()
        val navigator = LocalNavigator.currentOrThrow

        val welcomeScreen = rememberScreen(WelcomeDestination.Welcome)

        useEffect(true) {
            if (!uiState.value.hasSession) {
                navigator.replace(welcomeScreen)
            }
        }

        HomeScreenContent(
            homeDestinationEvent = homeDestinationEvent,
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalVoyagerApi::class)
@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    homeDestinationEvent: HomeDestinationEvent
) {

    val snackbarState = useSnackbar()
    val scope = useScope()

    useEffect(true) {
        when (homeDestinationEvent) {
            HomeDestinationEvent.WalletCreated -> scope.launch { snackbarState.showSnackbar("Wallet Created") }
            HomeDestinationEvent.None -> {}
        }
    }

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
                        if(tabNavigator.current.key == walletDetailTab.key) {
                            Text(text = tabNavigator.current.options.title)
                            Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = null)
                        } else {
                            Text(text = tabNavigator.current.options.title)
                        }
                    },
                )
            },
            content = {
                CurrentTab()
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
