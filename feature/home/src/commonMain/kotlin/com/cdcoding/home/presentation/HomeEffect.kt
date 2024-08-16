package com.cdcoding.home.presentation

sealed interface HomeEffect {
    data class Failure(val message: String) : HomeEffect
}
