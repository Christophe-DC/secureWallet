package com.cdcoding.wallet.client

import com.cdcoding.common.utils.decodeHex
import com.trustwallet.core.CoinType
import com.trustwallet.core.StoredKey


class StorePhraseClient(
   // private val context: Context,
) {
     suspend fun invoke(walletId: String, mnemonic: String, password: String): Result<Boolean> = try {
        val storedKey = StoredKey.importHDWallet(
            mnemonic,
            walletId,
            password.decodeHex(),
            CoinType.Bitcoin
        )
       // storedKey.store("$context.dataDir.toString()/$walletId")
        Result.success(true)
    } catch (err: Throwable) {
        Result.failure(err)
    }
}