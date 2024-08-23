package com.cdcoding.showphrase.presentation

sealed class ShowPhraseEvent {
    data class ShowToast(val message: String): ShowPhraseEvent()
}