package com.cdcoding.wallet.validator

interface PhraseValidator {
    operator fun invoke(data: String): Result<Boolean>
}

class InvalidWords(val words: List<String>): Exception()

object InvalidPhrase: Exception()