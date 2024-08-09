package com.cdcoding.core.designsystem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.cdcoding.core.designsystem.spacer.Spacer16
import com.cdcoding.core.resource.Res
import com.cdcoding.core.resource.common_try_again
import org.jetbrains.compose.resources.stringResource

@Composable
fun FatalErrorView(
    message: String,
    onTryAgain: (() -> Unit)? = null,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier,
                text = message,
            )
            if (onTryAgain != null) {
                Spacer16()
                Button(
                    modifier = Modifier,
                    onClick = onTryAgain
                ) {
                    Text(text = stringResource(Res.string.common_try_again))
                }
            }
        }
    }

}