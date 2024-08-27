package com.cdcoding.receiveasset.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.cdcoding.common.utils.ShareKit
import com.cdcoding.core.designsystem.button.FieldBottomAction
import com.cdcoding.core.designsystem.circularProgress.CircularProgressIndicator16
import com.cdcoding.core.designsystem.components.FatalErrorView
import com.cdcoding.core.designsystem.components.Scene
import com.cdcoding.core.designsystem.hooks.useInject
import com.cdcoding.core.designsystem.hooks.useScope
import com.cdcoding.core.designsystem.hooks.useSnackbar
import com.cdcoding.core.designsystem.spacer.Spacer16
import com.cdcoding.core.designsystem.state.collectAsStateWithLifecycle
import com.cdcoding.core.resource.Res
import com.cdcoding.core.resource.common_copy
import com.cdcoding.core.resource.common_share
import com.cdcoding.core.resource.copied_to_clipboard
import com.cdcoding.core.resource.receive_title
import com.cdcoding.model.AssetId
import com.cdcoding.receiveasset.presentation.ReceiveAssetState
import com.cdcoding.receiveasset.presentation.ReceiveAssetViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import qrgenerator.QRCodeImage


class ReceiveAssetScreen(private val assetId: AssetId) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val arguments = remember { mapOf("assetId" to assetId) }
        val viewModel: ReceiveAssetViewModel = useInject(arguments = arguments)
        val uiState = viewModel.uiState.collectAsStateWithLifecycle()

        ReceiveAssetScreenContent(
            uiState = uiState.value,
            popBackStack = { navigator.pop() }
        )
    }
}


@Composable
fun ReceiveAssetScreenContent(
    uiState: ReceiveAssetState,
    popBackStack: () -> Unit,
) {

    val clipboardManager = LocalClipboardManager.current
    val snackbarState = useSnackbar()
    val scope = useScope()

    Scene(
        title = stringResource(Res.string.receive_title, uiState.assetSymbol),
        onClose = popBackStack,
        snackbar = snackbarState
    ) {
        when {
            uiState.isLoading -> {
                CircularProgressIndicator16()
            }

            !uiState.errorMessage.isNullOrEmpty() -> {
                FatalErrorView(message = uiState.errorMessage)
            }

            uiState.address.isEmpty() || uiState.chain == null -> {}
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            ElevatedCard(
                                modifier = Modifier,
                                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White,
                                    contentColor = Color.White,
                                )
                            ) {
                                QRCodeImage(
                                    modifier = Modifier
                                        .widthIn(100.dp, 400.dp)
                                        .heightIn(100.dp, 400.dp)
                                        .padding(12.dp),
                                    url = uiState.address,
                                    contentDescription = "Receive QR",
                                    contentScale = ContentScale.FillWidth
                                )
                            }
                            Spacer16()
                            Text(
                                text = uiState.walletName,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.titleLarge,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = uiState.address,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.secondary,
                                style = MaterialTheme.typography.bodyLarge,
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Row {
                                FieldBottomAction(
                                    modifier = Modifier.weight(1f),
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "paste",
                                    text = stringResource(Res.string.common_copy),
                                ) {
                                    clipboardManager.setText(AnnotatedString(uiState.address))
                                    scope.launch {
                                        snackbarState.showSnackbar(getString(Res.string.copied_to_clipboard))
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                FieldBottomAction(
                                    modifier = Modifier.weight(1f),
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "share",
                                    text = stringResource(Res.string.common_share)
                                ) {
                                    ShareKit.shareText(uiState.address)
                                }
                            }
                            Spacer16()
                        }
                    }
                }
            }
        }
    }
}
