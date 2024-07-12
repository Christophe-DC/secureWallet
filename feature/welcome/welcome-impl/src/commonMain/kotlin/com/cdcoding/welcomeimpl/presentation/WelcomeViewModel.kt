package com.cdcoding.welcomeimpl.presentation

import androidx.lifecycle.ViewModel


class WelcomeViewModel()  : ViewModel() {


   fun onEvent(event: WelcomeEvent) {
      when(event) {
         WelcomeEvent.OnCreateNewWallet -> onCreateNewWallet()
         WelcomeEvent.OnImportWallet -> onImportWallet()
      }
   }

   private fun onCreateNewWallet() {

   }
   private fun onImportWallet() {

   }
}