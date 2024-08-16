package com.cdcoding.wallet.operator

import com.cdcoding.common.utils.DataDir
import com.cdcoding.common.utils.decodeHex
import com.cdcoding.model.Chain
import com.trustwallet.core.Derivation
import com.trustwallet.core.StoredKey


class SWLoadPrivateKeyOperator(
    private val keyStoreDir: DataDir,
) : LoadPrivateKeyOperator {
    override suspend fun invoke(walletId: String, chain: Chain, password: String): ByteArray? {
        val coinType = ChainTypeProxy().invoke(chain)
        val store = StoredKey.load("${keyStoreDir.path}/$walletId")
        val wallet = store?.wallet(password.decodeHex())
        val privateKey = when (chain) {
            Chain.Solana -> wallet?.getKeyDerivation(coinType, Derivation.SolanaSolana)
            else -> wallet?.getKey(coinType, coinType.derivationPath())
        }
        return privateKey?.data
    }
}