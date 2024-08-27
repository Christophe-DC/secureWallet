package com.cdcoding.common.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class CommonViewModel<State, Effect, Intent>: ViewModel() {

    private val initialState: State by lazy { createInitialState() }

    private val _uiState: MutableStateFlow<State> = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()

    private val intents: MutableSharedFlow<Intent> = MutableSharedFlow()

    private val _effect = Channel<Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        subscribeIntents()
    }

    private fun subscribeIntents() {
        viewModelScope.launch {
            intents.collect {
                handleIntent(it)
            }
        }
    }

    fun setIntent(intent: Intent) {
        viewModelScope.launch {
            intents.emit(intent)
        }
    }

    protected fun setState(reduce: State.() -> State) {
        _uiState.value = uiState.value.reduce()
    }

    protected fun setEvent(builder: () -> Effect) {
        viewModelScope.launch { _effect.send(builder()) }
    }

    protected abstract fun createInitialState(): State

    protected abstract fun handleIntent(intent: Intent)

}