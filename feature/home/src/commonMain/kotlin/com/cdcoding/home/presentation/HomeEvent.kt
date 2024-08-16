package com.cdcoding.home.presentation

sealed interface HomeEvent {
    data object OnClick : HomeEvent
}