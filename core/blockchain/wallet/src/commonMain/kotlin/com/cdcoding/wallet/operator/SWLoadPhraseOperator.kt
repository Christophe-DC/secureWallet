package com.cdcoding.wallet.operator

import com.cdcoding.common.utils.DataDir
import com.cdcoding.common.utils.decodeHex
import com.trustwallet.core.StoredKey


class SWLoadPhraseOperator(
    private val keyStoreDir: DataDir
) : LoadPhraseOperator {
    override suspend fun invoke(walletId: String, password: String): String? =
        StoredKey.load("${keyStoreDir.path}/$walletId")?.decryptMnemonic(password.decodeHex())
}