package com.cdcoding.network.client.ton

import com.cdcoding.common.utils.type
import com.cdcoding.model.AssetSubtype
import com.cdcoding.model.Chain
import com.cdcoding.model.SignerParams
import com.cdcoding.network.client.SignClient
import com.ionspin.kotlin.bignum.integer.BigInteger
import com.trustwallet.core.AnySigner
import com.trustwallet.core.CoinType
import com.trustwallet.core.sign
import com.trustwallet.core.theopennetwork.JettonTransfer
import com.trustwallet.core.theopennetwork.SendMode
import com.trustwallet.core.theopennetwork.SigningInput
import com.trustwallet.core.theopennetwork.SigningOutput
import com.trustwallet.core.theopennetwork.Transfer
import com.trustwallet.core.theopennetwork.WalletVersion
import io.ktor.utils.io.core.toByteArray
import okio.ByteString.Companion.toByteString


class TonSignClient : SignClient {
    override suspend fun signTransfer(
        params: SignerParams,
        privateKey: ByteArray
    ): ByteArray {
        if (params.input.assetId.type() == AssetSubtype.TOKEN) {
            return signToken(params, privateKey)
        }
        val signingInput = SigningInput(
            messages = listOf(
                Transfer(
                    wallet_version = WalletVersion.WALLET_V4_R2,
                    dest = params.input.destination(),
                    amount = params.finalAmount.longValue(),
                    comment = params.input.memo() ?: "",
                    //  sequenceNumber = (params.info as TonSignerPreloader.Info).sequence,
                    mode = SendMode.PAY_FEES_SEPARATELY.value or SendMode.IGNORE_ACTION_PHASE_ERRORS.value,
                    bounceable = false
                )
            ),
            sequence_number = (params.info as TonSignerPreloader.Info).sequence,
            private_key = privateKey.toByteString()
        )
        val output =
            AnySigner.sign(signingInput, CoinType.TON, SigningOutput.ADAPTER)
        return output.encoded.toByteArray()
    }

    override fun maintainChain(): Chain = Chain.Ton

    private fun signToken(params: SignerParams, privateKey: ByteArray): ByteArray {
        val meta = params.info as TonSignerPreloader.Info
        val jettonTransfer = JettonTransfer(
            // transfer = transfer,
            jetton_amount = params.finalAmount.longValue(),
            to_owner = params.input.destination(),
            response_address =  params.owner,
            forward_amount = 1
        )

        val transfer = Transfer(
            wallet_version = WalletVersion.WALLET_V4_R2,
            dest = meta.jettonAddress ?: "",
            amount = (meta.fee.options[tokenAccountCreationKey] ?: BigInteger.ZERO).longValue(),
            comment = params.input.memo() ?: "",
            jetton_transfer = jettonTransfer,
            mode =
                SendMode.PAY_FEES_SEPARATELY.value or SendMode.IGNORE_ACTION_PHASE_ERRORS.value,
            bounceable = true
        )

        val signingInput = SigningInput(
            messages = listOf(transfer),
            sequence_number = meta.sequence,
            private_key = privateKey.toByteString()
        )
        return AnySigner.sign(signingInput, CoinType.TON, SigningOutput.ADAPTER)
            .encoded.toByteArray()
    }
}