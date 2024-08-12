package com.cdcoding.model

import com.ionspin.kotlin.bignum.decimal.BigDecimal

enum class InputCurrency {

    InCrypto {

        override fun getAmount(value: String, decimals: Int, price: Double): Crypto =
            Crypto(value.numberParse(), decimals)

        override fun getInput(amount: Crypto?, decimals: Int, price: Double): String
            = amount?.value(decimals)?.toStringExpanded() ?: ""
    },

    InFiat {
        override fun getAmount(value: String, decimals: Int, price: Double): Crypto =
            Fiat(value.numberParse()).convert(decimals, price)

        override fun getInput(amount: Crypto?, decimals: Int, price: Double): String =
            amount?.convert(decimals, price)
                ?.value(decimals)?.toStringExpanded()
                ?: ""
    };

    abstract fun getAmount(value: String, decimals: Int, price: Double = 0.0): Crypto

    abstract fun getInput(amount: Crypto?, decimals: Int, price: Double): String
}

fun String.numberParse(): BigDecimal {
    val parts = replace(",", ".")
        .replace(" ", "")
        .split(".")
    val number = List(parts.size) { i ->
        "${parts[i]}${if (i + 1 == parts.size - 1) "." else ""}"
    }.joinToString("")
    return BigDecimal.parseString(number.trim().replace("\uFEFF", ""))
}