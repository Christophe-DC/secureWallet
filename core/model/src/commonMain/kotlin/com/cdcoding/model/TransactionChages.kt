package com.cdcoding.model

import com.ionspin.kotlin.bignum.integer.BigInteger


data class TransactionChages(
    val state: TransactionState,
    val fee: BigInteger? = null,
    val hashChanges: HashChanges? = null,
)

data class HashChanges(
    val old: String,
    val new: String,
)
