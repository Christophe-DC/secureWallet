package com.cdcoding.core.designsystem.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SWTextField(
    label: String? = null,
    hint: String = "",
    textValue: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onValueChanged: (String) -> Unit,
) {

    val focusRequester = remember { FocusRequester() }
    //val context = LocalContext.current

    val borderColor = if (isError) {
        MaterialTheme.colorScheme.error
    } else {
        MaterialTheme.colorScheme.primary
    }

    /*val cautionColor = if (state?.errorOrNull is FormsInputStateWarning) {
        ComposeAppTheme.colors.jacob
    } else {
        ComposeAppTheme.colors.lucian
    }*/

    Column(modifier) {
        if(!label.isNullOrEmpty()) {
            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = label,
                maxLines = 1
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 48.dp)
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, borderColor, RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerLow),
            verticalAlignment = Alignment.CenterVertically
        ) {

            /*var textState by rememberSaveable(initial, stateSaver = TextFieldValue.Saver) {
                mutableStateOf(TextFieldValue(initial ?: "", TextRange(initial?.length ?: 0)))
            }

            prefix?.let {
                body_grey(
                    modifier = Modifier.padding(start = 12.dp),
                    text = prefix
                )
            }*/

            BasicTextField(
                modifier = Modifier
                    .focusRequester(focusRequester)
                    /*.onFocusChanged {
                        onChangeFocus?.invoke(it.isFocused)
                    }*/
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .weight(1f),
                enabled = enabled,
                value = textValue,
                onValueChange = { newValue -> onValueChanged.invoke(newValue) },
                textStyle = MaterialTheme.typography.bodyLarge,
                singleLine = true,
                //  cursorBrush = SolidColor(ComposeAppTheme.colors.jacob),
                decorationBox = { innerTextField ->
                    if (textValue.isEmpty()) {
                        Text(
                            hint,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    innerTextField()
                },
                // visualTransformation = visualTransformation,
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions
            )

            Spacer(modifier = Modifier.width(28.dp))


            if (textValue.isNotEmpty()) {
                IconButton(onClick = {
                    onValueChanged.invoke("")
                    focusRequester.requestFocus()
                }) {
                    Icon(
                        tint = MaterialTheme.colorScheme.primary,
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "Clear"
                    )
                }
            }

        }
    }


    /*
    val interactionSource = remember { MutableInteractionSource() }

    BasicTextField(
        value = textValue,
        onValueChange = { newValue -> onValueChanged.invoke(newValue) },
        textStyle = MaterialTheme.typography.bodyLarge,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                shape = MaterialTheme.shapes.large
            ),
        interactionSource = interactionSource,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = true
    ) {
        TextFieldDefaults.DecorationBox(
            value = textValue,
            innerTextField = it,
            singleLine = true,
            enabled = true,
            visualTransformation = VisualTransformation.None,
            label = { Text(text = label ?: "") },
            trailingIcon = {
                if(textValue.isNotEmpty()) {
                    IconButton(onClick = { onValueChanged.invoke("") }) {
                        Icon(
                            tint = MaterialTheme.colorScheme.primary,
                            imageVector = Icons.Filled.Clear,
                            contentDescription = "Clear"
                        )
                    }
                }
            },
            placeholder = { /* ... */ },
            interactionSource = interactionSource,
            contentPadding = TextFieldDefaults.contentPaddingWithoutLabel(
                start = 8.dp,
                top = 4.dp
            )
        )
    }*/

}
