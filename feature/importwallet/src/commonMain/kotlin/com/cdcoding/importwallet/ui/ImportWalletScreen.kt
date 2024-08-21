package com.cdcoding.importwallet.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.cdcoding.core.designsystem.button.MainActionButton
import com.cdcoding.core.designsystem.components.Scene
import com.cdcoding.core.designsystem.hooks.useInject
import com.cdcoding.core.designsystem.spacer.Spacer16
import com.cdcoding.core.designsystem.spacer.Spacer8
import com.cdcoding.core.designsystem.state.collectAsStateWithLifecycle
import com.cdcoding.core.navigation.HomeDestination
import com.cdcoding.core.resource.Res
import com.cdcoding.core.resource.errors_create_wallet
import com.cdcoding.core.resource.errors_import_invalid_secret_phrase
import com.cdcoding.core.resource.errors_import_invalid_secret_phrase_word
import com.cdcoding.core.resource.errors_invalid_address_name
import com.cdcoding.core.resource.wallet_default_name
import com.cdcoding.core.resource.wallet_import_action
import com.cdcoding.core.resource.wallet_import_title
import com.cdcoding.core.resource.wallet_name
import com.cdcoding.data.repository.ImportError
import com.cdcoding.importwallet.presentation.ImportWalletIntent
import com.cdcoding.importwallet.presentation.ImportWalletState
import com.cdcoding.importwallet.presentation.ImportWalletViewModel
import com.cdcoding.importwallet.ui.component.ImportInput
import com.cdcoding.importwallet.ui.component.WalletNameTextField
import com.cdcoding.wallet.operator.SWFindPhraseWord
import org.jetbrains.compose.resources.stringResource


class ImportWalletScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val viewModel: ImportWalletViewModel = useInject()
        val uiState = viewModel.uiState.collectAsStateWithLifecycle()


        val homeScreenWithWalletCreatedEvent = rememberScreen(HomeDestination.Home)

        ImportWalletScreenContent(
            uiState = uiState.value,
            onIntent = viewModel::setIntent,
            popBackStack = { navigator.pop() },
            nextStack = { navigator.replaceAll(homeScreenWithWalletCreatedEvent) }
        )
    }
}


@Composable
fun ImportWalletScreenContent(
    uiState: ImportWalletState,
    onIntent: (ImportWalletIntent) -> Unit,
    popBackStack: () -> Unit,
    nextStack: () -> Unit,
) {

    var inputState by remember {
        mutableStateOf(TextFieldValue())
    }
    val walletName = stringResource(Res.string.wallet_default_name, uiState.generatedNameIndex)

    var nameState by remember(walletName + uiState.generatedNameIndex) {
        mutableStateOf(walletName)
    }
    var autoComplete by remember {
        mutableStateOf("")
    }
    var nameRecordState by remember(uiState.nameRecord?.address) {
        mutableStateOf(uiState.nameRecord)
    }
    val suggestions = remember {
        mutableStateListOf<String>()
    }
    var dataErrorState by remember(uiState.dataError) {
        mutableStateOf(uiState.dataError)
    }

    Scene(
        title = stringResource(Res.string.wallet_import_title),
        padding = PaddingValues(16.dp),
        mainActionPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 48.dp),
        onClose = popBackStack,
        mainAction = {
            MainActionButton(
                title = stringResource(Res.string.wallet_import_action),
                onClick = {
                    onIntent(
                        ImportWalletIntent.OnImport(
                            nameState,
                            walletName,
                            inputState.text,
                            nameRecordState,
                            nextStack
                        )
                    )
                },
            )
        },
    ) {
        WalletNameTextField(
            value = nameState,
            onValueChange = { newValue -> nameState = newValue },
            placeholder = stringResource(Res.string.wallet_name),
            error = uiState.walletNameError,
        )
        Spacer16()

        ImportInput(
            inputState = inputState,
            onValueChange = { query ->
                inputState = query
                suggestions.clear()
                dataErrorState = null

                if (suggestions.isNotEmpty()) {
                    return@ImportInput
                }

                val cursorPosition = query.selection.start
                if (query.text.isEmpty()) {
                    return@ImportInput
                }
                val word = query.text.substring(0..<cursorPosition).split(" ")
                    .lastOrNull()
                if (word.isNullOrEmpty()) {
                    return@ImportInput
                }
                val result = SWFindPhraseWord().invoke(word.toString())
                if (result.size == 1 && !autoComplete.contains(word)) {
                    autoComplete = result.first()
                    val processed = setSuggestion(inputState, result.first())
                    inputState = processed
                } else {
                    suggestions.addAll(result)
                }
            }
        ) {
            nameRecordState = it
        }

        if (suggestions.isNotEmpty()) {
            Spacer8()
            LazyRow {
                items(suggestions) { word ->
                    SuggestionChip(
                        onClick = {
                            val processed = setSuggestion(inputState, word)
                            inputState = processed
                            autoComplete = ""
                            suggestions.clear()
                        },
                        label = { Text(text = word) }
                    )
                    Spacer8()
                }
            }
        }

        val error = dataErrorState
        if (error != null) {
            Spacer(modifier = Modifier.size(4.dp))
            val text = when (error) {
                is ImportError.CreateError -> stringResource(
                    Res.string.errors_create_wallet,
                    error.message ?: ""
                )

                is ImportError.InvalidWords -> stringResource(
                    Res.string.errors_import_invalid_secret_phrase_word,
                    error.words.joinToString()
                )

                ImportError.InvalidationSecretPhrase ->
                    stringResource(Res.string.errors_import_invalid_secret_phrase)

                ImportError.InvalidAddress -> stringResource(Res.string.errors_invalid_address_name)
            }
            Text(text = text, color = MaterialTheme.colorScheme.error)
        }
    }
}


private fun setSuggestion(inputState: TextFieldValue, word: String): TextFieldValue {
    val cursorPosition = inputState.selection.start
    val inputFull = inputState.text
    val rightInput =
        inputState.text.substring(0..<cursorPosition)
    val leftInput = inputState.text.substring(cursorPosition)
    val lastInput = rightInput.split(" ").lastOrNull() ?: ""
    val phrase = rightInput.removeSuffix(lastInput)
    return TextFieldValue(
        text = inputFull.replaceRange(0, inputFull.length, "$phrase$word $leftInput"),
        selection = TextRange("$phrase$word ".length)
    )
}