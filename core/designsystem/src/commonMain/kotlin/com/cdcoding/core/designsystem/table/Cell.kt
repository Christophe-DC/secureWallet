package com.cdcoding.core.designsystem.table

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cdcoding.core.designsystem.text.MiddleEllipsisText
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.http.Url


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Cell(
    label: @Composable () -> Unit,
    data: @Composable () -> Unit,
    support: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
    trailingIcon: String? = null,
    action: (() -> Unit)? = null,
    showActionChevron: Boolean = true,
    longAction: (() -> Unit)? = null,
    actionIcon: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .combinedClickable(
                enabled = action != null || longAction != null,
                onClick = { action?.invoke() },
                onLongClick = { longAction?.invoke() }
            )
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                top = 16.dp,
                bottom = 16.dp,
                end = if (action == null) 16.dp else 8.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        label()
        Spacer(modifier = Modifier.width(20.dp))
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End,
        ) {
            data()
            support?.invoke()
        }
        if (trailing != null) {
            Spacer(modifier = Modifier.size(8.dp))
            trailing()
        }
        if (!trailingIcon.isNullOrEmpty()) {
            Spacer(modifier = Modifier.size(8.dp))
            KamelImage(
                resource = asyncPainterResource(data = Url(trailingIcon)),
                contentDescription = "",
                modifier = Modifier.size(20.dp),
            )
        }
        if (action != null) {
            if (showActionChevron) {
                actionIcon?.invoke() ?: Icon(
                    painter = rememberVectorPainter(image = Icons.Default.ChevronRight),
                    contentDescription = "open_provider_select",
                    tint = MaterialTheme.colorScheme.secondary,
                )
            }
        }
    }
}

@Composable
fun Cell(
    label: String,
    data: String,
    icon: String? = null,
    dataColor: Color? = null,
    support: String? = null,
    actionIcon: (@Composable () -> Unit)? = null,
    showActionChevron: Boolean = true,
    trailing: (@Composable () -> Unit)? = null,
    trailingIcon: String? = null,
    longAction: (() -> Unit)? = null,
    action: (() -> Unit)? = null,
) {
    Cell(
        label = {
            if (!icon.isNullOrEmpty()) {
                KamelImage(
                    resource = asyncPainterResource(data = Url(icon)),
                    contentDescription = "",
                    modifier = Modifier.size(24.dp),
                )
                Spacer(modifier = Modifier.size(8.dp))
            }
            Text(
                text = label,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge,
            )
        },
        data = {
            MiddleEllipsisText(
                modifier = Modifier,
                text = data,
                textAlign = TextAlign.End,
                color = dataColor ?: MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodyLarge,
            )
        },
        support = if (support != null) {
            {
                MiddleEllipsisText(
                    text = support,
                    textAlign = TextAlign.End,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        } else {
            null
        },
        trailing = trailing,
        trailingIcon = trailingIcon,
        action = action,
        showActionChevron = showActionChevron,
        longAction = longAction,
        actionIcon = actionIcon,
    )
}