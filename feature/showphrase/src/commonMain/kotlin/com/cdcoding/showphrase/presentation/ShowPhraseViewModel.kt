package com.cdcoding.showphrase.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.cdcoding.common.utils.CommonViewModel
import com.cdcoding.data.repository.WalletRepository
import com.cdcoding.datastore.password.PasswordStore
import com.cdcoding.wallet.operator.LoadPhraseOperator
import kotlinx.coroutines.launch

class ShowPhraseViewModel(
    savedStateHandle: SavedStateHandle,
    private val walletRepository: WalletRepository,
    private val passwordStore: PasswordStore,
    private val loadPhraseOperator: LoadPhraseOperator,
) : CommonViewModel<ShowPhraseUIState, ShowPhraseEvent, ShowPhraseIntent>() {


    override fun createInitialState(): ShowPhraseUIState {
        return ShowPhraseUIState()
    }

    override fun handleIntent(intent: ShowPhraseIntent) {}

    init {
        viewModelScope.launch {
            val walletId: String = savedStateHandle["walletId"] ?: return@launch
            val password = passwordStore.getPassword(walletId)
            val phrase = loadPhraseOperator(walletId, password)
            val wallet = walletRepository.getWallet(walletId)
                setState {
                    copy(
                        walletId = walletId,
                        walletName = wallet.name,
                        words = if (phrase.isNullOrEmpty()) emptyList() else phrase.split(" "),
                    )
                }
        }
    }

}