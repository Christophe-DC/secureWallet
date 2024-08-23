package com.cdcoding.showphrase.di

import cafe.adriel.voyager.core.registry.screenModule
import com.cdcoding.core.navigation.ShowPhraseDestination
import com.cdcoding.showphrase.ui.ShowPhraseScreen

val showPhraseScreenModule = screenModule {
    register<ShowPhraseDestination.ShowPhrase> { provider -> ShowPhraseScreen(provider.walletId) }
}
