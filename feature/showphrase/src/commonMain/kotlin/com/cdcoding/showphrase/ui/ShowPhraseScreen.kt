package com.cdcoding.showphrase.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.cdcoding.core.designsystem.components.Scene
import com.cdcoding.core.designsystem.hooks.useInject
import com.cdcoding.core.designsystem.hooks.useScope
import com.cdcoding.core.designsystem.hooks.useSnackbar
import com.cdcoding.core.designsystem.spacer.Spacer16
import com.cdcoding.core.designsystem.spacer.Spacer8
import com.cdcoding.core.designsystem.state.collectAsStateWithLifecycle
import com.cdcoding.core.resource.Res
import com.cdcoding.core.resource.common_copy
import com.cdcoding.core.resource.common_secret_phrase
import com.cdcoding.core.resource.copied_to_clipboard
import com.cdcoding.core.resource.secret_phrase_do_not_share_description
import com.cdcoding.core.resource.secret_phrase_do_not_share_title
import com.cdcoding.showphrase.presentation.ShowPhraseUIState
import com.cdcoding.showphrase.presentation.ShowPhraseViewModel
import com.cdcoding.showphrase.ui.component.PhraseLayout
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource


class ShowPhraseScreen(private val walletId: String) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val arguments = remember { mapOf("walletId" to walletId) }
        val viewModel: ShowPhraseViewModel = useInject(arguments = arguments)
        val uiState = viewModel.uiState.collectAsStateWithLifecycle()

        ShowPhraseScreenContent(
            uiState = uiState.value,
            popBackStack = { navigator.pop() },
        )
    }
}


@Composable
fun ShowPhraseScreenContent(
    uiState: ShowPhraseUIState,
    popBackStack: () -> Unit,
) {

    val snackbarState = useSnackbar()
    val scope = useScope()
    val clipboardManager = LocalClipboardManager.current
    Scene(
        title = stringResource(Res.string.common_secret_phrase),
        padding = PaddingValues(16.dp),
        onClose = popBackStack,
        snackbar = snackbarState
    ) {
        Column(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.error.copy(alpha = 0.1f),
                    shape = MaterialTheme.shapes.small
                )
                .padding(16.dp)
            ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(Res.string.secret_phrase_do_not_share_title),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
            )
            Spacer8()
            Text(
                text = stringResource(Res.string.secret_phrase_do_not_share_description),
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
            )
        }
        Spacer16()
        Spacer16()
        PhraseLayout(words = uiState.words)
        Spacer16()
        TextButton(
            onClick = {
                clipboardManager.setText(AnnotatedString(uiState.words.joinToString(" ")))
                scope.launch {
                    snackbarState.showSnackbar(getString(Res.string.copied_to_clipboard))
                }
            }

        ) {
            Text(text = stringResource(Res.string.common_copy))
        }
    }
}
