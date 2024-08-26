package com.cdcoding.welcome.presentation

import androidx.lifecycle.viewModelScope
import com.cdcoding.common.utils.CommonViewModel
import com.cdcoding.data.repository.SessionRepository
import kotlinx.coroutines.launch


class WelcomeViewModel(
    sessionRepository: SessionRepository
) : CommonViewModel<WelcomeUIState, WelcomeEffect, WelcomeIntent>() {


    override fun createInitialState(): WelcomeUIState = WelcomeUIState()

    override fun handleIntent(intent: WelcomeIntent) {
        when (intent) {
            WelcomeIntent.OnCreateNewWallet -> onCreateNewWallet()
            WelcomeIntent.OnImportWallet -> onImportWallet()
        }
    }

    init {
        viewModelScope.launch {
            sessionRepository.session().collect { session ->
                val hasSession = session != null
                setState { copy(hasSession = hasSession) }
            }
        }
    }

    private fun onCreateNewWallet() {}

    private fun onImportWallet() {}
}