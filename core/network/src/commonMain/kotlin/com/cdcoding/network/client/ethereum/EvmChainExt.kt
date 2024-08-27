package com.cdcoding.network.client.ethereum

import com.cdcoding.common.utils.decodeHex
import com.cdcoding.common.utils.type
import com.cdcoding.model.AssetId
import com.cdcoding.model.AssetSubtype
import com.cdcoding.model.Chain
import com.cdcoding.model.EVMChain
import com.cdcoding.wallet.operator.ChainTypeProxy
import com.ionspin.kotlin.bignum.integer.BigInteger
import com.trustwallet.core.AnyAddress
import com.trustwallet.core.EthereumAbi
import com.trustwallet.core.EthereumAbiFunction


fun EVMChain.Companion.encodeTransactionData(
    assetId: AssetId,
    memo: String?,
    amount: BigInteger,
    destinationAddress: String,
): ByteArray {
    return if (assetId.type() != AssetSubtype.NATIVE && memo.isNullOrEmpty()) {
        val abiFn = EthereumAbiFunction("transfer").apply {
            addParamAddress(AnyAddress(destinationAddress, ChainTypeProxy().invoke(assetId.chain)).data, false)
            addParamUInt256(amount.toByteArray(), false)
        }
        EthereumAbi.encode(abiFn)
    } else {
        memo?.decodeHex() ?: byteArrayOf()
    }
}

fun EVMChain.Companion.getDestinationAddress(
    assetId: AssetId,
    destinationAddress: String,
): String {
    return when (assetId.type()) {
        AssetSubtype.NATIVE -> destinationAddress
        AssetSubtype.TOKEN -> assetId.tokenId!!
    }
}

fun EVMChain.Companion.isOpStack(chain: Chain): Boolean {
    return when (chain) {
        Chain.Optimism,
        Chain.Base,
        Chain.OpBNB -> true
        else -> false
    }
}