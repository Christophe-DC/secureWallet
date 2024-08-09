package com.cdcoding.core.designsystem.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.cdcoding.core.designsystem.text.TextPlaceholder
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
fun AssetIcon(
    iconUrl: String,
    placeholderText: String?,
    supportIcon: String?,
    errorImageVector: ImageVector? = null,
    modifier: Modifier = Modifier.size(40.dp),
) {
    Box {
        KamelImage(
            resource = asyncPainterResource(data = iconUrl),
            contentDescription = "list_item_icon",
            modifier = modifier,
            onLoading = {
                if (!placeholderText.isNullOrEmpty()) {
                    TextPlaceholder(
                        text = placeholderText,
                        circleColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.45f)
                    )
                }
            },
            onFailure = {
                if (errorImageVector != null) {
                    rememberVectorPainter(image = errorImageVector)
                } else {
                    if (!placeholderText.isNullOrEmpty()) {
                        TextPlaceholder(
                            text = placeholderText,
                            circleColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.45f)
                        )
                    }

                }
            }
        )
        if (!supportIcon.isNullOrEmpty()) {
            KamelImage(
                resource = asyncPainterResource(data = supportIcon),
                contentDescription = "list_item_support_icon",
                modifier = Modifier
                    .size(18.dp)
                    .align(Alignment.BottomEnd)
                    .border(0.5.dp, MaterialTheme.colorScheme.surface, CircleShape),
            )
        }
    }
}