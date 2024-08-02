package com.cdcoding.createwalletimpl.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cdcoding.domain.CreateWalletUseCase
import com.cdcoding.domain.GetNextWalletNumberUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class CreateWalletViewModel(
    private val createWalletUseCase: CreateWalletUseCase,
    private val getNextWalletNumberUseCase: GetNextWalletNumberUseCase
) : ViewModel() {


    private val _state = MutableStateFlow(CreateWalletState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<CreateWalletEffect>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: CreateWalletEvent) {
        when (event) {
            CreateWalletEvent.OnCreateNewWallet -> onCreateNewWallet()
            is CreateWalletEvent.OnWalletNameChanged -> onWalletNameChanged(event.name)
        }
    }

    init {

        viewModelScope.launch {
            val defaultWalletName = "Wallet ${getNextWalletNumberUseCase()}"
            _state.value = state.value.copy(
                defaultWalletName = defaultWalletName,
                walletName = defaultWalletName
            )
        }
    }

    private fun onCreateNewWallet() {
        viewModelScope.launch {
            _state.value = state.value.copy(walletIsCreating = true)
            runCatching { createWalletUseCase(_state.value.walletName) }
                .onSuccess { result ->
                    _state.value = state.value.copy(walletIsCreating = false)
                    _eventFlow.emit(
                        CreateWalletEffect.WalletCreated
                    )
                }
                .onFailure {
                    _state.value = state.value.copy(walletIsCreating = false)
                    val failureMessage = it.message ?: "Error is occurred."
                    _eventFlow.emit(
                        CreateWalletEffect.Failure(message = failureMessage)
                    )
                }
        }
    }

    private fun onWalletNameChanged(name: String) {
        viewModelScope.launch {
            _state.value = state.value.copy(walletName = name)
        }
    }
}