package com.cdcoding.createwallet.presentation

import androidx.lifecycle.viewModelScope
import com.cdcoding.common.utils.CommonViewModel
import com.cdcoding.domain.CreateWalletUseCase
import com.cdcoding.domain.GetNextWalletNumberUseCase
import kotlinx.coroutines.launch


class CreateWalletViewModel(
    private val createWalletUseCase: CreateWalletUseCase,
    private val getNextWalletNumberUseCase: GetNextWalletNumberUseCase
) : CommonViewModel<CreateWalletState, CreateWalletEffect, CreateWalletIntent>() {

    override fun createInitialState(): CreateWalletState = CreateWalletState()

    override fun handleIntent(intent: CreateWalletIntent) {
        when (intent) {
            is CreateWalletIntent.OnCreateNewWallet -> onCreateNewWallet()
            is CreateWalletIntent.OnWalletNameChanged -> onWalletNameChanged(intent.name)
        }
    }

    init {

        viewModelScope.launch {
            val defaultWalletName = "Wallet ${getNextWalletNumberUseCase()}"
            setState {
                copy(
                    defaultWalletName = defaultWalletName,
                    walletName = defaultWalletName
                )
            }
        }
    }

    private fun onCreateNewWallet() {
        viewModelScope.launch {
            setState { copy(walletIsCreating = true) }
            runCatching { createWalletUseCase(uiState.value.walletName) }
                .onSuccess {
                    setState { copy(walletIsCreating = false) }
                    setEvent { CreateWalletEffect.WalletCreated }
                }
                .onFailure {
                    setState { copy(walletIsCreating = false) }
                    it.printStackTrace()
                    val failureMessage = it.message ?: "Error is occurred."
                    setEvent { CreateWalletEffect.Failure(message = failureMessage) }
                }
        }
    }

    private fun onWalletNameChanged(name: String) {
        setState { copy(walletName = name) }
    }
}