package com.cdcoding.walletdetailimpl.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cdcoding.walletdetailimpl.presentation.PriceState
import com.cdcoding.walletdetailimpl.presentation.PriceUIState


@Composable
fun PriceInfo(
    priceValue: String,
    changedPercentages: String,
    state: PriceState,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.secondary,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    isHighlightPercentage: Boolean = false,
    internalPadding: Dp = 16.dp,
) {
    val highlightColor = Color.Red //priceColor(state)
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = priceValue,
            color = if (isHighlightPercentage) highlightColor else color,
            style = style,
        )
        Spacer(modifier = Modifier.width(internalPadding))
        Text(
            modifier = if (isHighlightPercentage) {
                Modifier.background(highlightColor.copy(alpha = 0.15f), MaterialTheme.shapes.small)
            } else {
                Modifier
            }.padding(4.dp),
            text = changedPercentages,
            color = highlightColor,
            style = style,
        )
    }
}


@Composable
fun PriceInfo(
    price: PriceUIState,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    isHighlightPercentage: Boolean = false,
    internalPadding: Dp = 16.dp,
) {
    val color = Color.Red // priceColor(price.state)
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = price.value,
            color = if (isHighlightPercentage) color else MaterialTheme.colorScheme.secondary,
            style = style,
        )
        Spacer(modifier = Modifier.width(internalPadding))
        Text(
            modifier = if (isHighlightPercentage) {
                Modifier.background(color.copy(alpha = 0.15f), MaterialTheme.shapes.small)
            } else {
                Modifier
            }.padding(4.dp),
            text = price.dayChanges,
            color = color,
            style = style,
        )
    }
}