package com.cdcoding.core.designsystem.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cdcoding.core.designsystem.text.TextPlaceholder
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.http.Url


@Composable
fun HeaderIcon(
    iconUrl: String? = null,
    supportIconUrl: String? = null,
    placeholder: String?  = null,
    iconSize: Dp = 64.dp
) {
    if (iconUrl == null) {
        return
    }
    Box {
        KamelImage(
            resource = asyncPainterResource(data = Url(iconUrl)),
            contentDescription = "header_icon",
            modifier = Modifier.size(iconSize),
            onLoading = {
                if (!placeholder.isNullOrEmpty()) {
                    TextPlaceholder(
                        text = placeholder,
                        circleColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.45f)
                    )
                }
            },
            onFailure = {
                if (!placeholder.isNullOrEmpty()) {
                    TextPlaceholder(
                        text = placeholder,
                        circleColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.45f)
                    )
                }
            }
        )
        if (!supportIconUrl.isNullOrEmpty()) {
            KamelImage(
                resource = asyncPainterResource(data = Url(supportIconUrl)),
                contentDescription = "header_support_icon",
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.BottomEnd)
                    .border(0.5.dp, MaterialTheme.colorScheme.surface, CircleShape),
            )
        }

    }
}