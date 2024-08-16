package com.cdcoding.welcome.presentation

import com.cdcoding.common.utils.CommonViewModel


class WelcomeViewModel : CommonViewModel<WelcomeUIState, WelcomeEffect, WelcomeIntent>() {


    override fun createInitialState(): WelcomeUIState = WelcomeUIState()

    override fun handleIntent(intent: WelcomeIntent) {
        when (intent) {
            WelcomeIntent.OnCreateNewWallet -> onCreateNewWallet()
            WelcomeIntent.OnImportWallet -> onImportWallet()
        }
    }

    private fun onCreateNewWallet() {}

    private fun onImportWallet() {}
}