package com.cdcoding.model

import com.ionspin.kotlin.bignum.integer.BigInteger


data class SignerParams(
    val input: ConfirmParams,
    val finalAmount: BigInteger = BigInteger.ZERO,
    val owner: String,
    val info: SignerInputInfo,
)

interface  SignerInputInfo {
    fun fee(): Fee
}