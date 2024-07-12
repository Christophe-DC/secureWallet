package com.cdcoding.welcomeimpl.presentation

sealed interface WelcomeEvent {
    object OnCreateNewWallet : WelcomeEvent
    object OnImportWallet : WelcomeEvent
}