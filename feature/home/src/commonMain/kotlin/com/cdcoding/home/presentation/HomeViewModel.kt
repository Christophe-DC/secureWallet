package com.cdcoding.home.presentation

import androidx.lifecycle.viewModelScope
import com.cdcoding.common.utils.CommonViewModel
import com.cdcoding.data.repository.SessionRepository
import kotlinx.coroutines.launch


class HomeViewModel(
    private val sessionRepository: SessionRepository,
) : CommonViewModel<HomeUIState, HomeEffect, HomeIntent>() {

    override fun createInitialState(): HomeUIState = HomeUIState()

    override fun handleIntent(intent: HomeIntent) {}

    init {
        viewModelScope.launch {
            sessionRepository.session().collect { session ->
                println("session :$session")
                val hasSession = session != null
                setState { copy(hasSession = hasSession) }
            }
        }
    }
}