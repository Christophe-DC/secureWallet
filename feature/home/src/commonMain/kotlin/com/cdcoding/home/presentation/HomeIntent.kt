package com.cdcoding.home.presentation

sealed interface HomeIntent {
    data object OnClick : HomeIntent
}