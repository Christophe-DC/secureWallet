package com.cdcoding.wallet.operator

import com.cdcoding.model.Chain


interface LoadPrivateKeyOperator {
    suspend operator fun invoke(walletId: String, chain: Chain, password: String): ByteArray?
}