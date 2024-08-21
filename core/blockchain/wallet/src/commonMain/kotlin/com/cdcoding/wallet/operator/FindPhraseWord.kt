package com.cdcoding.wallet.operator

interface FindPhraseWord {
    operator fun invoke(query: String): List<String>
}