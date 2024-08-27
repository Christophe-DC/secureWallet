package com.cdcoding.importwallet.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.unit.dp
import com.cdcoding.core.designsystem.button.FieldBottomAction
import com.cdcoding.core.designsystem.circularProgress.CircularProgressIndicator16
import com.cdcoding.core.designsystem.components.AddressChainViewModel
import com.cdcoding.core.designsystem.hooks.useInject
import com.cdcoding.core.designsystem.spacer.Spacer16
import com.cdcoding.core.designsystem.state.collectAsStateWithLifecycle
import com.cdcoding.core.resource.Res
import com.cdcoding.core.resource.common_paste
import com.cdcoding.core.resource.wallet_import_secret_phrase
import com.cdcoding.model.NameRecord
import com.cdcoding.wallet.operator.InvalidWords
import com.cdcoding.wallet.operator.SWValidatePhraseOperator
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ImportInput(
    inputState: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    onResolved: (NameRecord?) -> Unit,
) {
    val viewModel: AddressChainViewModel = useInject()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DisposableEffect(Unit) {
        viewModel.onResolved(onResolved)

        onDispose { }
    }

    val errorColor =  MaterialTheme.colorScheme.error
    val clipboardManager = LocalClipboardManager.current
    val interactionSource = remember { MutableInteractionSource() }

    val indicatorColor = OutlinedTextFieldDefaults.colors().indicatorColor(
        enabled = true,
        isError = false,
        interactionSource = interactionSource
    )
    val focused by interactionSource.collectIsFocusedAsState()
    val targetThickness = if (focused) OutlinedTextFieldDefaults.FocusedBorderThickness else OutlinedTextFieldDefaults.UnfocusedBorderThickness
    val animatedThickness = animateDpAsState(targetThickness, tween(durationMillis = 150), "")
    val borderStroke by rememberUpdatedState(
        BorderStroke(animatedThickness.value, SolidColor(indicatorColor.value))
    )

    Column(
        modifier = Modifier
            .border(borderStroke, OutlinedTextFieldDefaults.shape)
            .padding(16.dp),
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            BasicTextField(
                modifier = Modifier.fillMaxWidth(),
                onValueChange = onValueChange,
                value = inputState,
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                minLines = 3,
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                visualTransformation = {
                    TransformedText(
                        highlightErrors(
                            it.text,
                            errorColor = errorColor
                        ),
                        OffsetMapping.Identity
                    )
                },
                decorationBox = { innerTextField ->
                    if (inputState.text.isEmpty()) {
                        Text(
                            text = stringResource(Res.string.wallet_import_secret_phrase),
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray.copy(alpha = 0.5f),
                        )
                    }
                    innerTextField()
                },
                keyboardOptions = KeyboardOptions(
                    autoCorrect = false
                ),
                interactionSource = interactionSource,
            )
            Row(
                modifier = Modifier.align(Alignment.TopEnd),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator16()
                    Spacer(modifier = Modifier.size(8.dp))
                }
                if (uiState.isResolve) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Name is resolved",
                        tint = MaterialTheme.colorScheme.tertiary,
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                }
                if (uiState.isFail) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.Default.Error,
                        contentDescription = "Name is resolved",
                        tint = MaterialTheme.colorScheme.error,
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                }
            }
        }
        Spacer16()
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            FieldBottomAction(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .align(Alignment.CenterEnd),
                imageVector = Icons.Default.ContentPaste,
                contentDescription = "paste",
                text = stringResource(Res.string.common_paste),
            ) {
                val newValue = clipboardManager.getText()?.text ?: ""
                onValueChange(
                    TextFieldValue("$newValue ", TextRange(newValue.length + 1))
                )
            }
        }
    }
}

private fun highlightErrors(text: String, errorColor: Color): AnnotatedString {
    val validateResult = SWValidatePhraseOperator().invoke(text)
    val error = validateResult.exceptionOrNull()
    val inputWords = text.split(" ")
    val spans = if (error is InvalidWords) {
        error.words.filter {
            text.indexOf("$it ") != -1
        }.map { word ->
            val ranges = mutableListOf<AnnotatedString.Range<SpanStyle>>()
            var offset = 0
            for (i in inputWords.indices) {
                val inputWord = inputWords[i]
                val end = offset + inputWord.length + 1
                if (inputWord == word && end <= text.length && end != offset) {
                    ranges.add(
                        AnnotatedString.Range(
                            item = SpanStyle(color = errorColor),
                            start = offset,
                            end = end,
                        )
                    )
                }
                offset = end
            }
            ranges
        }.flatten()
    } else {
        emptyList()
    }
    return AnnotatedString(text, spans)
}

@Composable
private fun TextFieldColors.indicatorColor(
    enabled: Boolean,
    isError: Boolean,
    interactionSource: InteractionSource
): State<Color> {
    val focused by interactionSource.collectIsFocusedAsState()

    val targetValue = when {
        !enabled -> disabledIndicatorColor
        isError -> errorIndicatorColor
        focused -> focusedIndicatorColor
        else -> unfocusedIndicatorColor
    }
    return if (enabled) {
        animateColorAsState(targetValue, tween(durationMillis = 150), label = "")
    } else {
        rememberUpdatedState(targetValue)
    }
}