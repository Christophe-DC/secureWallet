package com.cdcoding.shared

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.cdcoding.amount.di.amountScreenModule
import com.cdcoding.confirm.di.confirmScreenModule
import com.cdcoding.core.navigation.tab.registry.TabRegistry
import com.cdcoding.createwallet.di.createWalletScreenModule
import com.cdcoding.editwallet.di.editWalletScreenModule
import com.cdcoding.showphrase.di.showPhraseScreenModule
import com.cdcoding.home.di.homeScreenModule
import com.cdcoding.home.ui.HomeScreen
import com.cdcoding.importwallet.di.importWalletScreenModule
import com.cdcoding.receiveasset.di.receiveAssetScreenModule
import com.cdcoding.selectasset.di.selectAssetScreenModule
import com.cdcoding.sendasset.di.sendAssetScreenModule
import com.cdcoding.system.ui.theme.SecureWalletTheme
import com.cdcoding.walletdetail.di.walletDetailScreenModule
import com.cdcoding.welcome.di.welcomeScreenModule
import com.cdcoding.selectwallet.di.selectWalletScreenModule


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
                selectAssetScreenModule()
                sendAssetScreenModule()
                amountScreenModule()
                confirmScreenModule()
                receiveAssetScreenModule()
                importWalletScreenModule()
                selectWalletScreenModule()
                editWalletScreenModule()
                showPhraseScreenModule()
            }

            TabRegistry {
                walletDetailScreenModule()
            }

            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .safeDrawingPadding()
            ) {
                Navigator(screen = HomeScreen()) {navigator ->
                    SlideTransition(navigator)
                }
            }
      //  }
    }
}