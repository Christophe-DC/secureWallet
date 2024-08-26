package com.cdcoding.transactions.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cdcoding.common.utils.getIconUrl
import com.cdcoding.common.utils.getSupportIconUrl
import com.cdcoding.common.utils.getSwapMetadata
import com.cdcoding.core.designsystem.components.TransactionItem
import com.cdcoding.model.Crypto
import com.cdcoding.model.TransactionExtended
import com.ionspin.kotlin.bignum.integer.toBigInteger
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.TransactionsList(
    items: List<TransactionExtended>,
) {
    /*val dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM)
    var prev = 0L
    val calendar = Calendar.getInstance()*/

    var prev: LocalDateTime? = null

    items.forEachIndexed { index, item ->
        /*calendar.timeInMillis = item.transaction.createdAt
        calendar[Calendar.MILLISECOND] = 999
        calendar[Calendar.SECOND] = 59
        calendar[Calendar.MINUTE] = 59
        calendar[Calendar.HOUR_OF_DAY] = 23
        val createdAt = calendar.time.time*/

        val createdAt = Instant.fromEpochMilliseconds(item.transaction.createdAt)
            .toLocalDateTime(TimeZone.currentSystemDefault())
        val endOfDay = createdAt.date.atTime(23, 59, 59, 999_999_999)

        if (prev == null || prev!!.date != createdAt.date) {
            stickyHeader {
                val title =   "${createdAt.date.year}-${createdAt.date.monthNumber.toString().padStart(2, '0')}-${createdAt.date.dayOfMonth.toString().padStart(2, '0')}"
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.background)
                        .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 8.dp),
                    text = title
                )
            }
        }
        item(key = item.transaction.id) {
            TransactionItem(
                assetIcon = item.asset.getIconUrl(),
                supportIcon = item.asset.getSupportIconUrl(),
                assetSymbol = item.asset.symbol,
                to = item.transaction.to,
                from = item.transaction.from,
                direction = item.transaction.direction,
                type = item.transaction.type,
                state = item.transaction.state,
                value = Crypto(item.transaction.value.toBigInteger()).format(
                    item.asset.decimals,
                    item.asset.symbol,
                    2,
                    dynamicPlace = true,
                ),
                metadata = item.transaction.getSwapMetadata(),
                assets = item.assets,
                isLast = index == items.size - 1
            )
        }
        prev = endOfDay
    }
}