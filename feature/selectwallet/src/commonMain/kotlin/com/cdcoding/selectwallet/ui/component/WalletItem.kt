package com.cdcoding.selectwallet.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cdcoding.core.designsystem.components.ListItem
import com.cdcoding.core.designsystem.components.ListItemTitle
import com.cdcoding.core.designsystem.spacer.Spacer16
import com.cdcoding.core.designsystem.spacer.Spacer8
import com.cdcoding.model.IconUrl
import org.jetbrains.compose.ui.tooling.preview.Preview


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WalletItem(
    id: String,
    name: String,
    icon: IconUrl,
    isCurrent: Boolean,
    onSelect: ((String) -> Unit)? = null,
    onMenu: ((String) -> Unit)? = null,
    onEdit: ((String) -> Unit)? = null,
) {
    ListItem(
        modifier = Modifier
            .combinedClickable(
                enabled = onSelect != null || onMenu != null,
                onClick = { onSelect?.invoke(id) },
                onLongClick = { onMenu?.invoke(id) }
            )
            .heightIn(72.dp),
        iconUrl = icon,
        placeholder = name.firstOrNull()?.toString(),
        supportIcon =  null,
        trailing = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer16()
                if (isCurrent) {
                    Icon(
                        imageVector = Icons.Default.CheckCircleOutline,
                        contentDescription = "checked",
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
                if (onEdit != null) {
                    Spacer8()
                    IconButton(onClick = { onEdit(id) }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "edit",
                            tint = MaterialTheme.colorScheme.secondary,
                        )
                    }
                }
            }
        }
    ) {
        ListItemTitle(
            title = name,
            titleBudge = null
        )
    }
}

@Preview
@Composable
fun PreviewWalletItem() {
    MaterialTheme {
        WalletItem(
            id = "1",
            name = "Foo wallet name",
            icon = "",
            isCurrent = true,
            onSelect = {},
            onEdit = {},
            onMenu = {},
        )
    }
}