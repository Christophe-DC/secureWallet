package com.cdcoding.wallet.client

import com.cdcoding.common.utils.uuid4
import com.cdcoding.model.Account
import com.cdcoding.model.Chain
import com.cdcoding.wallet.operator.ChainTypeProxy
import com.trustwallet.core.Derivation
import com.trustwallet.core.HDWallet

interface WalletClient {
    suspend fun createWallet(): String
    suspend fun createWallet(data: String, chain: Chain): Account
}


class DefaultWalletClient() :  WalletClient{

    override suspend fun createWallet(): String {
        return HDWallet(128, "").mnemonic
    }

    override suspend fun createWallet(data: String, chain: Chain): Account {
        val hdWallet = HDWallet(data, "")
        val coinType = ChainTypeProxy().invoke(chain = chain)
        val address = if (chain == Chain.Solana) {
            hdWallet.getAddressDerivation(coinType, Derivation.SolanaSolana)
        } else {
            hdWallet.getAddressForCoin(coinType)
        }
        val extendedPublicKey = if (chain == Chain.Solana) {
            hdWallet.getExtendedPublicKeyDerivation(coinType.purpose, coinType,
                Derivation.SolanaSolana, coinType.xpubVersion)
        } else {
            hdWallet.getExtendedPublicKey(coinType.purpose, coinType, coinType.xpubVersion)
        }
        return Account(
            id = uuid4(),
            chain = chain,
            address = address,
            derivationPath = coinType.derivationPath(),
            extendedPublicKey = extendedPublicKey
        )
    } //Result<String> = Result.success(HDWallet(128, "").mnemonic())
}