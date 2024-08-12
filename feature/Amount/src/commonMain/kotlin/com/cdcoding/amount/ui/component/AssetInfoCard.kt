package com.cdcoding.amount.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cdcoding.common.utils.getIconUrl
import com.cdcoding.common.utils.type
import com.cdcoding.core.designsystem.components.ListItem
import com.cdcoding.core.designsystem.components.ListItemTitle
import com.cdcoding.core.resource.Res
import com.cdcoding.core.resource.transfer_balance
import com.cdcoding.core.resource.transfer_max
import com.cdcoding.model.AssetId
import com.cdcoding.model.AssetSubtype
import com.cdcoding.model.AssetType
import org.jetbrains.compose.resources.stringResource

@Composable
fun AssetInfoCard(
    assetId: AssetId,
    assetIcon: String,
    assetTitle: String,
    assetType: AssetType,
    availableAmount: String,
    onMaxAmount: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        ListItem(
            iconUrl = assetIcon,
            supportIcon = if (assetId.type() != AssetSubtype.NATIVE) {
                assetId.chain.getIconUrl()
            } else { null },
            placeholder = assetType.string,
            dividerShowed = false,
            trailing = {
                Button(
                    onClick = onMaxAmount,
                    colors = ButtonDefaults.buttonColors().copy(
                        containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.09f),
                        contentColor = MaterialTheme.colorScheme.onSurface,
                    ),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(text = stringResource(Res.string.transfer_max))
                }
            }
        ) {
            ListItemTitle(title = assetTitle, subtitle = stringResource(Res.string.transfer_balance, availableAmount))
        }
        HorizontalDivider(thickness = 0.4.dp)
    }
}