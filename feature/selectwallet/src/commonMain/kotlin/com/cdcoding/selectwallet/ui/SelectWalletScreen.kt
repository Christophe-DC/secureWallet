package com.cdcoding.selectwallet.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.cdcoding.core.designsystem.components.Scene
import com.cdcoding.core.designsystem.hooks.useInject
import com.cdcoding.core.designsystem.spacer.Spacer16
import com.cdcoding.core.designsystem.state.collectAsStateWithLifecycle
import com.cdcoding.core.navigation.CreateWalletDestination
import com.cdcoding.core.navigation.HomeDestination
import com.cdcoding.core.navigation.ImportWalletDestination
import com.cdcoding.core.navigation.WelcomeDestination
import com.cdcoding.core.resource.Res
import com.cdcoding.core.resource.common_cancel
import com.cdcoding.core.resource.common_delete
import com.cdcoding.core.resource.common_wallet
import com.cdcoding.core.resource.create_wallet
import com.cdcoding.core.resource.import_wallet
import com.cdcoding.core.resource.wallet_delete_wallet_confirmation
import com.cdcoding.core.resource.wallets_title
import com.cdcoding.selectwallet.presentation.SelectWalletIntent
import com.cdcoding.selectwallet.presentation.SelectWalletUIState
import com.cdcoding.selectwallet.presentation.SelectWalletViewModel
import com.cdcoding.selectwallet.ui.component.WalletItem
import com.cdcoding.system.ui.theme.largeMarginDimens
import com.cdcoding.system.ui.theme.mediumMarginDimens
import org.jetbrains.compose.resources.stringResource


class SelectWalletScreen: Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val viewModel: SelectWalletViewModel = useInject()
        val uiState = viewModel.uiState.collectAsStateWithLifecycle()

        val createWalletScreen = rememberScreen(CreateWalletDestination.CreateWallet)
        val importWalletScreen = rememberScreen(ImportWalletDestination.ImportWallet)
        val welcomeScreen = rememberScreen(WelcomeDestination.Welcome)
        val homeScreen = rememberScreen(HomeDestination.Home)

        SelectWalletScreenContent(
            uiState = uiState.value,
            onIntent = viewModel::setIntent,
            popBackStack = { navigator.pop() },
            onCreateWalletClick = { navigator.push(createWalletScreen) },
            onImportWalletClick = { navigator.push(importWalletScreen) },
            onEdit = {},
            onBoard = { navigator.replaceAll(welcomeScreen) },
            onSelectWallet = { navigator.replaceAll(homeScreen) },
        )
    }
}


@Composable
fun SelectWalletScreenContent(
    uiState: SelectWalletUIState,
    onIntent: (SelectWalletIntent) -> Unit,
    popBackStack: () -> Unit,
    onCreateWalletClick: () -> Unit,
    onImportWalletClick: () -> Unit,
    onEdit: (walletId: String) -> Unit,
    onBoard: () -> Unit,
    onSelectWallet: () -> Unit,
) {

    var deleteWalletId by remember { mutableStateOf("") }
    var longPressedWallet by remember { mutableStateOf("") }

    Scene(
        title = stringResource(Res.string.wallets_title),
        onClose = popBackStack
    ) {
        LazyColumn {
            item {
                Spacer16()
                Card(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = largeMarginDimens.margin)
                        .clickable { onCreateWalletClick() },

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
            }
            item {
                Spacer16()
                Card(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = largeMarginDimens.margin)
                        .clickable { onImportWalletClick() },
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
                Spacer16()
            }
            items(items = uiState.wallets, key = { it.id }) { wallet ->
                var itemWidth by remember { mutableIntStateOf(0) }
                Box(
                    modifier = Modifier.onSizeChanged { itemWidth = it.width }
                ) {
                    WalletItem(
                        id = wallet.id,
                        name = wallet.name,
                        icon = wallet.icon,
                        isCurrent = wallet.id == uiState.currentWalletId,
                        onSelect = { walletId ->
                            onIntent(SelectWalletIntent.OnWalletSelected(walletId))
                            onSelectWallet()
                        },
                        onMenu = { walletId -> longPressedWallet = walletId },
                        onEdit = { walletId -> onEdit(walletId) },
                    )
                    DropdownMenu(
                        expanded = longPressedWallet == wallet.id,
                        offset = DpOffset((with(LocalDensity.current) { itemWidth.toDp() } / 2), 8.dp),
                        onDismissRequest = { longPressedWallet = "" },
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(Res.string.common_wallet)) },
                            trailingIcon = { Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "wallet_config"
                            )
                            },
                            onClick = {
                                onEdit(longPressedWallet)
                                longPressedWallet = ""
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = stringResource(Res.string.common_delete),
                                    color = MaterialTheme.colorScheme.error
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    tint = MaterialTheme.colorScheme.error,
                                    contentDescription = "wallet_config"
                                )
                            },
                            onClick = {
                                deleteWalletId = wallet.id
                                longPressedWallet = ""
                            }
                        )
                    }
                }
            }
        }
    }
    if (deleteWalletId.isNotEmpty()) {
        AlertDialog(
            text = {
                Text(
                    text = stringResource( Res.string.wallet_delete_wallet_confirmation,
                        uiState.wallets.firstOrNull{ it.id == deleteWalletId}?.name ?: "" ),
                )
            },
            onDismissRequest = { deleteWalletId = "" },
            confirmButton = {
                TextButton(
                    onClick = {
                        val walletId = deleteWalletId
                        deleteWalletId = ""
                        onIntent(SelectWalletIntent.OnDeleteWallet(walletId, onBoard))
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(text = stringResource(Res.string.common_delete))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { deleteWalletId = "" },
                ) {
                    Text(text = stringResource(Res.string.common_cancel))
                }
            },
        )
    }
}
