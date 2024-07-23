package com.cdcoding.homeimpl.presentation

sealed interface HomeEvent {
    data object OnClick : HomeEvent
}