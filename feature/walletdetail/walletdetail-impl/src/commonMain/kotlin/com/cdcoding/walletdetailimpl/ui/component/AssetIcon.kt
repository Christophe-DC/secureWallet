package com.cdcoding.walletdetailimpl.ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
fun AssetIcon(
    iconUrl: String,
    placeholder: String?,
    supportIcon: String?,
    modifier: Modifier = Modifier.size(40.dp),
) {
    Box {
        KamelImage(
            resource = asyncPainterResource(data = iconUrl),
            contentDescription = "list_item_icon",
            modifier = modifier,
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