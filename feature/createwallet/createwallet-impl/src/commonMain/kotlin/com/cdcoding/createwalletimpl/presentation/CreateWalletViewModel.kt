package com.cdcoding.createwalletimpl.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdcoding.wallet.WalletClient
import kotlinx.coroutines.launch


class CreateWalletViewModel(val walletClient: WalletClient)  : ViewModel() {

   fun onEvent(event: CreateWalletEvent) {
      when(event) {
         CreateWalletEvent.OnCreateNewWallet -> onCreateNewWallet()
      }
   }

   private fun onCreateNewWallet() {
      viewModelScope.launch {

         val result = walletClient.createWallet().getOrNull()
         println("result: $result")
      }
   }
}