package com.cdcoding.securewallet

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.cdcoding.securewallet.di.initKoin

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "SecureWallet",
        ) {
            App(
                darkTheme = false,
                dynamicColor = false
            )
        }
    }
}