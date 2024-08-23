package com.cdcoding.wallet.operator

import com.cdcoding.common.utils.DataDir
import com.cdcoding.common.utils.decodeHex
import com.trustwallet.core.CoinType
import com.trustwallet.core.StoredKey


class SWStorePhraseOperator(
    private val keyStoreDir: DataDir,
) : StorePhraseOperator {
    override suspend fun invoke(walletId: String, mnemonic: String, password: String): Result<Boolean> = try {
        val storedKey = StoredKey.importHDWallet(
            mnemonic,
            walletId,
            password.decodeHex(),
            CoinType.Bitcoin
        )
        val result = storedKey?.store("${keyStoreDir.path}/$walletId") ?: false
        println("SWStorePhraseOperator result: $result")
        Result.success(result)
    } catch (err: Throwable) {
        Result.failure(err)
    }
}