package com.cdcoding.network.client.xrp

import com.cdcoding.model.Chain
import com.cdcoding.model.SignerParams
import com.cdcoding.network.client.SignClient
import com.cdcoding.wallet.operator.ChainTypeProxy
import com.trustwallet.core.AnySigner
import com.trustwallet.core.ripple.OperationPayment
import com.trustwallet.core.ripple.SigningInput
import com.trustwallet.core.ripple.SigningOutput
import com.trustwallet.core.sign
import okio.ByteString.Companion.toByteString


class XrpSignClient(
    private val chain: Chain,
) : SignClient {
    override suspend fun signTransfer(params: SignerParams, privateKey: ByteArray): ByteArray {
        val metadata = params.info as XrpSignerPreloader.Info
        val signInput = SigningInput(
            fee = metadata.fee().amount.longValue(),
            sequence =  metadata.sequence,
            account = params.owner,
            private_key = privateKey.toByteString(),
            op_payment = OperationPayment(
                destination =  params.input.destination(),
                amount = params.finalAmount.longValue(),
                destination_tag = try { params.input.memo()?.toLong() ?: 0L } catch (err: Throwable) { 0L }
            )
        )
        val output = AnySigner.sign(signInput, ChainTypeProxy().invoke(chain), SigningOutput.ADAPTER)
        return output.encoded.toByteArray()
    }

    override fun maintainChain(): Chain = chain
}