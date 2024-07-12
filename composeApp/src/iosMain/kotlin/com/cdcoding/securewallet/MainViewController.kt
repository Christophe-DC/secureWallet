package com.cdcoding.securewallet

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIScreen
import platform.UIKit.UIUserInterfaceStyle
import com.cdcoding.securewallet.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
)
{
    val isDarkTheme =
        UIScreen.mainScreen.traitCollection.userInterfaceStyle == UIUserInterfaceStyle.UIUserInterfaceStyleDark
    App(
        darkTheme = isDarkTheme,
        dynamicColor = false
    )
}