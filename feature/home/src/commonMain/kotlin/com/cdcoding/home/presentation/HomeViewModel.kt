package com.cdcoding.home.presentation

import com.cdcoding.common.utils.CommonViewModel
import com.cdcoding.domain.GetHasSessionUseCase


class HomeViewModel(
    private val getHasSessionUseCase: GetHasSessionUseCase,
) : CommonViewModel<HomeUIState, HomeEffect, HomeIntent>() {

    override fun createInitialState(): HomeUIState = HomeUIState()

    override fun handleIntent(intent: HomeIntent) {}

    init {
        setState { copy(hasSession = getHasSessionUseCase()) }
    }
}