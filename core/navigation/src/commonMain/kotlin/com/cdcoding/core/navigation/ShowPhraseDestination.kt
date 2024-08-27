package com.cdcoding.core.navigation

import com.cdcoding.core.navigation.core.Destination


sealed interface ShowPhraseDestination : Destination {
    data class ShowPhrase(val walletId: String) : ShowPhraseDestination
}