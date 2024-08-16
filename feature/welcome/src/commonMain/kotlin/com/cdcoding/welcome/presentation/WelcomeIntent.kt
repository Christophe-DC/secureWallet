package com.cdcoding.welcome.presentation

sealed interface WelcomeIntent {
    object OnCreateNewWallet : WelcomeIntent
    object OnImportWallet : WelcomeIntent
}