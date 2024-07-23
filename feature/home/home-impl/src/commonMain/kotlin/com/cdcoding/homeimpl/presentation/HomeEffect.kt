package com.cdcoding.homeimpl.presentation

sealed interface HomeEffect {
    data class Failure(val message: String) : HomeEffect
}
