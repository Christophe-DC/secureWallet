package com.cdcoding.wallet.validator

import com.trustwallet.core.Mnemonic

class DefaultPhraseValidator : PhraseValidator {
    override fun invoke(data: String): Result<Boolean> {
        val invalidWords = data.split(" ")
            .mapNotNull {
                if (Mnemonic.isValidWord(it)) {
                    return@mapNotNull null
                }
                it
            }
        if (invalidWords.isNotEmpty()) {
            return Result.failure(InvalidWords(invalidWords))
        }
        return if (Mnemonic.isValid(data)) {
            Result.success(true)
        } else {
            Result.failure(InvalidPhrase)
        }
    }
}