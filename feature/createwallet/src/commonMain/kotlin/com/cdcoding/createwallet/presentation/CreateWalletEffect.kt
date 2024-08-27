package com.cdcoding.createwallet.presentation

sealed interface CreateWalletEffect {
    data class Failure(val message: String) : CreateWalletEffect
    data object WalletCreated : CreateWalletEffect
}
