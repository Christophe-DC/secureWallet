package com.cdcoding.wallet.operator

interface ValidatePhraseOperator {
    operator fun invoke(data: String): Result<Boolean>
}

class InvalidWords(val words: List<String>): Exception()

object InvalidPhrase: Exception()