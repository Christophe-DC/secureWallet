package com.cdcoding.transactions.presentation

import androidx.lifecycle.viewModelScope
import com.cdcoding.common.utils.CommonViewModel
import com.cdcoding.data.repository.SessionRepository
import com.cdcoding.data.repository.TransactionRepository
import com.cdcoding.domain.SyncTransactionUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

class TransactionsViewModel(
    private val sessionRepository: SessionRepository,
    private val transactionRepository: TransactionRepository,
    private val syncTransactionUseCase: SyncTransactionUseCase,
) : CommonViewModel<TransactionsUIState, TransactionsEvent, TransactionsIntent>() {


    override fun createInitialState(): TransactionsUIState {
        return TransactionsUIState()
    }

    override fun handleIntent(intent: TransactionsIntent) {}

    init {
        viewModelScope.launch {
            sessionRepository.session().collect {
                transactionRepository.getTransactions(null).collect {
                    setState {
                        copy(
                            loading = false,
                            transactions = it.filterNotNull(),
                        )
                    }
                }
            }
        }
        refresh()
    }


    fun refresh() = viewModelScope.launch(Dispatchers.IO) {
        setState { copy(loading = true)  }
        syncTransactionUseCase(sessionRepository.getSession()?.wallet?.index?.toInt() ?: return@launch)
        setState { copy(loading = false)  }
    }

}