package com.cdcoding.network.client.bitcoin

import com.cdcoding.common.utils.decodeHex
import com.cdcoding.model.Chain
import com.cdcoding.model.GasFee
import com.cdcoding.model.SignerParams
import com.cdcoding.model.bitcoin.BitcoinUTXO
import com.cdcoding.network.client.SignClient
import com.cdcoding.wallet.operator.ChainTypeProxy
import com.trustwallet.core.AnySigner
import com.trustwallet.core.BitcoinMessageSigner
import com.trustwallet.core.BitcoinScript
import com.trustwallet.core.BitcoinSigHashType
import com.trustwallet.core.CoinType
import com.trustwallet.core.bitcoin.OutPoint
import com.trustwallet.core.bitcoin.SigningInput
import com.trustwallet.core.bitcoin.SigningOutput
import com.trustwallet.core.bitcoin.UnspentTransaction
import com.trustwallet.core.common.SigningError
import com.trustwallet.core.sign
import okio.ByteString
import okio.ByteString.Companion.toByteString


class BitcoinSignClient(
    private val chain: Chain,
) : SignClient {

    @OptIn(ExperimentalStdlibApi::class)
    override suspend fun signTransfer(
        params: SignerParams,
        privateKey: ByteArray
    ): ByteArray {
        val metadata = params.info as BitcoinSignerPreloader.Info
        val coinType = ChainTypeProxy().invoke(maintainChain())
        val gasFee = metadata.fee as GasFee
        val utxo = metadata.utxo.getUtxoTransactions(params.owner, coinType)
        val scripts = mutableMapOf<String, ByteString>()
        metadata.utxo.forEach {
            val redeemScript = BitcoinScript.lockScriptForAddress(params.owner, coinType)
            val scriptData = redeemScript.data
            if (coinType == CoinType.Bitcoin || scriptData.isNotEmpty()) {
                return@forEach
            }
            val keyHash = if (redeemScript.isPayToWitnessPublicKeyHash) {
                redeemScript.matchPayToWitnessPublicKeyHash()
            } else {
                redeemScript.matchPayToPubkeyHash()
            }?.toHexString()
            if (keyHash != null) {
                scripts[keyHash] = redeemScript.data.toByteString()
            }
        }
        val signingInput = SigningInput(
            coin_type = coinType.value.toInt(),
            hash_type = BitcoinSigHashType.All.value.toInt(),//BitcoinScript.hashTypeForCoin(coinType)
            amount = params.finalAmount.longValue(),
            byte_fee = gasFee.maxGasPrice.longValue(),
            to_address = params.input.destination(),
            change_address = params.owner,
            use_max_amount = params.input.isMax(),
            private_key = listOf(privateKey.toByteString()),
            utxo = utxo,
            scripts = scripts
        )
        signingInput.plan

        val output = AnySigner.sign(signingInput, coinType, SigningOutput.ADAPTER)
        if (output.error != SigningError.OK) {
            throw IllegalStateException(output.error.name)
        }
        if (output.error_message.isNotEmpty()) {
            throw IllegalStateException(output.error_message)
        }
        return output.encoded.toByteArray()
    }

    override fun maintainChain(): Chain = chain
}

fun List<BitcoinUTXO>.getUtxoTransactions(
    address: String,
    coinType: CoinType
): List<UnspentTransaction> {
    return map { utxo ->
        val hash = utxo.txid.decodeHex()
        hash.reverse()
        val outPoint = OutPoint(
            hash =  hash.toByteString(),
            index =  utxo.vout
        )
        UnspentTransaction(
            out_point = outPoint,
            script = BitcoinScript.lockScriptForAddress(address, coinType).data.toByteString()
        )
    }
}