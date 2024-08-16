package com.cdcoding.network.client.aptos

import com.cdcoding.model.Chain
import com.cdcoding.model.GasFee
import com.cdcoding.model.SignerParams
import com.cdcoding.network.client.SignClient
import com.cdcoding.wallet.operator.ChainTypeProxy
import com.trustwallet.core.AnySigner
import com.trustwallet.core.aptos.SigningInput
import com.trustwallet.core.aptos.SigningOutput
import com.trustwallet.core.aptos.TransferMessage
import com.trustwallet.core.sign
import io.ktor.utils.io.core.toByteArray
import okio.ByteString.Companion.toByteString


class AptosSignClient(
    private val chain: Chain,
) : SignClient {
    override suspend fun signTransfer(params: SignerParams, privateKey: ByteArray): ByteArray {
        val coinType = ChainTypeProxy().invoke(chain)
        val metadata = params.info as AptosSignerPreloader.Info
        val fee = (metadata.fee() as? GasFee) ?: throw Exception("Fee error")
        val signInput = SigningInput(
            chain_id = 1,
            transfer = TransferMessage(
                to =  params.input.destination(),
                amount = params.finalAmount.longValue()
            ),
            expiration_timestamp_secs = 3664390082,
            gas_unit_price = fee.maxGasPrice.longValue(),
            max_gas_amount =  fee.limit.longValue(),
            sequence_number =  metadata.sequence,
            sender =  params.owner,
            private_key = privateKey.toByteString()
        )
        val output = AnySigner.sign(signInput, coinType, SigningOutput.ADAPTER)
        return output.json.toByteArray()
    }

    override fun maintainChain(): Chain = chain
}