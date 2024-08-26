package com.cdcoding.transactions.di

import com.cdcoding.core.navigation.TransactionsDestination
import com.cdcoding.core.navigation.tab.registry.tabModule
import com.cdcoding.transactions.ui.TransactionsScreen


val transactionsScreenModule = tabModule {
    register<TransactionsDestination.Transactions> { TransactionsScreen() }
}
