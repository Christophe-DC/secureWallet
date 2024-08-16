package com.cdcoding.core.designsystem.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cdcoding.model.PriceState

@Composable
fun WalletDetailHeader(
    amount: String,
    equivalent: String? = null,
    iconUrl: String? = null,
    supportIconUrl: String? = null,
    placeholder: String? = null,
    changedValue: String? = null,
    changedPercentages: String? = null,
    changeState: PriceState = PriceState.None,
    actions: (@Composable () -> Unit)? = null,
) {
    Column {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            HeaderIcon(iconUrl = iconUrl, supportIconUrl = supportIconUrl, placeholder = placeholder)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = amount,
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            if (!equivalent.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = equivalent,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.W400,
                )
            }
            if (changedValue != null) {
                Spacer(modifier = Modifier.height(16.dp))
                PriceInfo(
                    priceValue = changedValue,
                    changedPercentages = changedPercentages ?: "",
                    state = changeState,
                    isHighlightPercentage = true,
                )
            }
            if (actions != null) {
                Spacer(modifier = Modifier.height(16.dp))
                actions()
            }
        }
        Spacer(modifier = Modifier.size(14.dp))
        HorizontalDivider(thickness = 0.4.dp)
    }
}
