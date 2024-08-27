package com.cdcoding.core.navigation

import com.cdcoding.core.navigation.core.TabDestination


sealed interface TransactionsDestination : TabDestination {
    data object Transactions : TransactionsDestination
}