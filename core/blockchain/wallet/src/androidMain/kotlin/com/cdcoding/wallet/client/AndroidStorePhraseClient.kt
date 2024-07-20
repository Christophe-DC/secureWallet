package com.cdcoding.wallet.client

import android.content.Context
import com.cdcoding.common.utils.decodeHex
import wallet.core.jni.CoinType
import wallet.core.jni.StoredKey

class AndroidStorePhraseClient(
    private val context: Context,
) : StorePhraseClient {
    override suspend fun invoke(walletId: String, mnemonic: String, password: String): Result<Boolean> = try {
        val storedKey = StoredKey.importHDWallet(
            mnemonic,
            walletId,
            password.decodeHex(),
            CoinType.BITCOIN
        )
        storedKey.store("$context.dataDir.toString()/$walletId")
        Result.success(true)
    } catch (err: Throwable) {
        Result.failure(err)
    }
}