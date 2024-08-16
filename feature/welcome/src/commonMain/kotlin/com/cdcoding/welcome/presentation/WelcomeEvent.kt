package com.cdcoding.welcome.presentation

sealed interface WelcomeEvent {
    object OnCreateNewWallet : WelcomeEvent
    object OnImportWallet : WelcomeEvent
}