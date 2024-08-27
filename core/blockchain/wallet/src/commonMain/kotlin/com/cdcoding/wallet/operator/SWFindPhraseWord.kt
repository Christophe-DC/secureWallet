package com.cdcoding.wallet.operator

import com.trustwallet.core.Mnemonic


class SWFindPhraseWord : FindPhraseWord {
    override fun invoke(query: String): List<String> {
        val words = Mnemonic.suggest(query).split(" ")
        return if (words.size == 1 && words.first().isEmpty()) {
            emptyList()
        } else  {
            words
        }
    }
}