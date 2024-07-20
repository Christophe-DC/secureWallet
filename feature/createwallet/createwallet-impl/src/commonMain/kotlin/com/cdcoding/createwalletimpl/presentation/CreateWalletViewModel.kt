package com.cdcoding.createwalletimpl.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cdcoding.domain.GetCreateWalletUseCase
import kotlinx.coroutines.launch


class CreateWalletViewModel(private val getCreateWalletUseCase: GetCreateWalletUseCase)  : ViewModel() {

   fun onEvent(event: CreateWalletEvent) {
      when(event) {
         CreateWalletEvent.OnCreateNewWallet -> onCreateNewWallet()
      }
   }

   private fun onCreateNewWallet() {
      viewModelScope.launch {

        // val result = getCreateWalletUseCase().getOrNull()
         runCatching { getCreateWalletUseCase("test") }
            .onSuccess { result ->
               println("result: ${result.getOrNull()}")
              // next.invoke(AddNewPasswordWish.InsertSuccess("Successfully added!"))
            }
            .onFailure {
               val failureMessage = it.message ?: "Error is occurred."
               //next.invoke(AddNewPasswordWish.InsertFailed(failureMessage))
            }
      }
   }
}