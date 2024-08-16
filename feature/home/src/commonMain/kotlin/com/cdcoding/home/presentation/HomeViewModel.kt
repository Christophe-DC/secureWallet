package com.cdcoding.home.presentation

import androidx.lifecycle.ViewModel
import com.cdcoding.domain.GetHasSessionUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow


class HomeViewModel(
   private val getHasSessionUseCase: GetHasSessionUseCase,
)  : ViewModel() {


   private val _state = MutableStateFlow(HomeState(hasSession = getHasSessionUseCase()))
   val state = _state.asStateFlow()

   private val _eventFlow = MutableSharedFlow<HomeEffect>()
   val eventFlow = _eventFlow.asSharedFlow()


   fun onEvent(event: HomeEvent) {
      /*when(event) {
         HomeEvent.OnClick -> TODO()
      }*/
   }
}