package com.cdcoding.securewallet

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.navigator.Navigator
import com.cdcoding.core.navigation.tab.registry.TabRegistry
import com.cdcoding.createwalletdi.createWalletScreenModule
import com.cdcoding.homedi.homeScreenModule
import com.cdcoding.homeimpl.ui.HomeScreen
import com.cdcoding.system.ui.theme.SecureWalletTheme
import com.cdcoding.walletdetaildi.walletDetailScreenModule
import com.cdcoding.welcomedi.welcomeScreenModule
import com.cdcoding.welcomeimpl.ui.WelcomeScreen
import org.koin.compose.KoinContext


@Composable
fun App(
    darkTheme: Boolean,
    dynamicColor: Boolean
) {
    SecureWalletTheme(darkTheme, dynamicColor) {
       // KoinContext {

            ScreenRegistry {
                welcomeScreenModule()
                homeScreenModule()
                createWalletScreenModule()
              //  importWalletScreenModule()
            }

            TabRegistry {
                walletDetailScreenModule()
            }

            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .safeDrawingPadding()
            ) {
                Navigator(screen = HomeScreen())
            }
      //  }
    }
}