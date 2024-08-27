package com.cdcoding.core.designsystem.table

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

data class CellEntity(
    val label: StringResource,
    val data: String,
    val dataColor: Color? = null,
    val support: String? = null,
    val actionIcon: (@Composable () -> Unit)? = null,
    val trailingIcon: String? = null,
    val icon: String? = null,
    val trailing: (@Composable () -> Unit)? = null,
    val dropDownActions: (@Composable (() -> Unit) -> Unit)? = null,
    val showActionChevron: Boolean = true,
    val action: (() -> Unit)? = null,
)

@Composable
fun Table(
    items: List<CellEntity?>,
) {
    var isDropDownShow by remember { mutableStateOf<CellEntity?>(null) }
    val items = items.mapNotNull { it }
    if (items.isEmpty()) {
        return
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        Column {
            for (i in items.indices) {
                val item = items[i]
                Box {
                    Cell(
                        label = stringResource(item.label),
                        icon = item.icon,
                        data = item.data,
                        dataColor = item.dataColor,
                        support = item.support,
                        action = item.action,
                        longAction = if (item.dropDownActions == null) null else {
                            { isDropDownShow = item }
                        },
                        actionIcon = item.actionIcon,
                        showActionChevron = item.showActionChevron,
                        trailingIcon = item.trailingIcon,
                        trailing = item.trailing,
                    )
                    DropdownMenu(
                        modifier = Modifier.align(Alignment.BottomEnd),
                        expanded = isDropDownShow == item && item.dropDownActions != null,
                        offset = DpOffset(16.dp, 8.dp),
                        onDismissRequest = { isDropDownShow = null },
                    ) {
                        item.dropDownActions?.invoke {
                            isDropDownShow = null
                        }
                    }
                }
                if (i < items.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        thickness = 0.4.dp
                    )
                }
            }
        }
        HorizontalDivider(thickness = 0.4.dp)
    }
}
