package com.cdcoding.walletdetailimpl.presentation


import com.cdcoding.model.AssetId
import com.cdcoding.model.AssetMetaData
import com.cdcoding.model.AssetPrice
import com.cdcoding.model.AssetType
import com.cdcoding.model.Currency
import com.cdcoding.model.Fiat
import kotlin.math.absoluteValue
import kotlin.math.round

typealias IconUrl = String

data class AssetUIState(
    val id: AssetId,
    val name: String,
    val icon: IconUrl,
    val symbol: String,
    val type: AssetType,
    val value: String,
    val isZeroValue: Boolean,
    val price: PriceUIState? = null,
    val fiat: String = "",
    val owner: String = "",
    val metadata: AssetMetaData? = null,
)

data class PriceUIState(
    val value: String,
    val state: PriceState,
    val dayChanges: String,
) {
    companion object {
        fun create(price: AssetPrice?, currency: Currency): PriceUIState {
            val value = if (price == null || price.price == 0.0) {
                ""
            } else {
                Fiat(price.price).format(0, currency.string, 2, dynamicPlace = true)
            }
            val dayChanges = formatPercentage(price?.priceChangePercentage24h ?: 0.0)
            val state = getState(price?.priceChangePercentage24h ?: 0.0)
            return PriceUIState(value = value, state = state, dayChanges = dayChanges)
        }


        fun formatPercentage(value: Double, showSign: Boolean = true, showZero: Boolean = false): String {
            if (value == 0.0 && !showZero) {
                return ""
            }

            val formattedValue = round(value.absoluteValue * 100).toInt() / 100.0

            val sign = when {
                showSign && value > 0 -> "+"
                showSign && value < 0 -> "-"
                else -> ""
            }

            return sign + "${if (formattedValue == 0.0) if (showZero) "0.00" else "" else formattedValue}%"
        }

        fun getState(percentage: Double): PriceState {

            val formattedValue = round(percentage * 100).toInt() / 100.0
            return when {
                formattedValue > 0 -> PriceState.Up
                formattedValue < 0 -> PriceState.Down
                else -> PriceState.None
            }
        }
    }
}

enum class PriceState {
    None,
    Up,
    Down,
}
