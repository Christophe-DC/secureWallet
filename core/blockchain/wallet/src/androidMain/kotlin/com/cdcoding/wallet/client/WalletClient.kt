package com.cdcoding.wallet.client

import com.cdcoding.wallet.model.Account
import com.cdcoding.wallet.model.Chain
import com.cdcoding.wallet.operator.ChainTypeProxy
import wallet.core.jni.Derivation
import wallet.core.jni.HDWallet

actual class WalletClient {

    actual suspend fun createWallet(): String {
        return HDWallet(128, "").mnemonic()
    }

    actual suspend fun createWallet(data: String, chain: Chain): Account {
        val hdWallet = HDWallet(data, "")
        val coinType = ChainTypeProxy().invoke(chain = chain)
        val address = if (chain == Chain.Solana) {
            hdWallet.getAddressDerivation(coinType, Derivation.SOLANASOLANA)
        } else {
            hdWallet.getAddressForCoin(coinType)
        }
        val extendedPublicKey = if (chain == Chain.Solana) {
            hdWallet.getExtendedPublicKeyDerivation(coinType.purpose(), coinType,
                Derivation.SOLANASOLANA, coinType.xpubVersion())
        } else {
            hdWallet.getExtendedPublicKey(coinType.purpose(), coinType, coinType.xpubVersion())
        }
        return Account(
            chain = chain,
            address = address,
            derivationPath = coinType.derivationPath(),
            extendedPublicKey = extendedPublicKey
        )
    } //Result<String> = Result.success(HDWallet(128, "").mnemonic())
}