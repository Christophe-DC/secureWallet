package com.cdcoding.editwallet.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.cdcoding.common.utils.CommonViewModel
import com.cdcoding.common.utils.getIconUrl
import com.cdcoding.data.repository.WalletRepository
import com.cdcoding.domain.DeleteWalletUseCase
import kotlinx.coroutines.launch

class EditWalletViewModel(
    savedStateHandle: SavedStateHandle,
    private val walletRepository: WalletRepository,
    private val deleteWalletUseCase: DeleteWalletUseCase,
) : CommonViewModel<EditWalletUIState, EditWalletEvent, EditWalletIntent>() {


    override fun createInitialState(): EditWalletUIState {
        return EditWalletUIState()
    }

    override fun handleIntent(intent: EditWalletIntent) {
        when (intent) {
            is EditWalletIntent.SetWalletName -> setWalletName(intent.walletName)
            is EditWalletIntent.OnDeleteWallet -> handleDeleteWallet(intent.onBoard)
        }
    }

    init {
        viewModelScope.launch {
            val walletId: String? = savedStateHandle["walletId"]
            if (walletId == null) {
                setState { copy(error = "Wallet id not found") }
                return@launch
            }
            val wallet = walletRepository.getWallet(walletId)
                setState {
                    copy(
                        walletId = walletId,
                        walletName = wallet.name,
                        walletAddress = wallet.accounts.firstOrNull()?.address ?: "",
                        chainIconUrl = wallet.accounts.firstOrNull()?.chain?.getIconUrl() ?: "",
                    )
                }
        }
    }


    private fun setWalletName(name: String) {
        viewModelScope.launch {
            val wallet = walletRepository.getWallet(uiState.value.walletId)
            walletRepository.updateWallet(wallet.copy(name = name))
        }
    }

    private fun handleDeleteWallet(onBoard: () -> Unit) = viewModelScope.launch {
        deleteWalletUseCase(uiState.value.walletId, onBoard)
    }

}