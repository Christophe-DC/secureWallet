package com.cdcoding.network.client.cosmo

import com.cdcoding.common.utils.type
import com.cdcoding.model.AssetSubtype
import com.cdcoding.model.Chain
import com.cdcoding.model.ConfirmParams
import com.cdcoding.model.GasFee
import com.cdcoding.model.SignerParams
import com.cdcoding.model.TransactionType
import com.cdcoding.model.cosmos.CosmosDenom
import com.cdcoding.model.cosmos.from
import com.cdcoding.network.client.SignClient
import com.cdcoding.wallet.operator.ChainTypeProxy
import com.ionspin.kotlin.bignum.integer.BigInteger
import com.trustwallet.core.AnyAddress
import com.trustwallet.core.AnySigner
import com.trustwallet.core.CoinType
import com.trustwallet.core.cosmos.Amount
import com.trustwallet.core.cosmos.BroadcastMode
import com.trustwallet.core.cosmos.Fee
import com.trustwallet.core.cosmos.Message
import com.trustwallet.core.cosmos.SigningInput
import com.trustwallet.core.cosmos.SigningMode
import com.trustwallet.core.cosmos.SigningOutput
import com.trustwallet.core.sign
import io.ktor.utils.io.core.toByteArray
import okio.ByteString.Companion.toByteString

class CosmosSignClient(
    private val chain: Chain,
) : SignClient {

    override suspend fun signTransfer(
        params: SignerParams,
        privateKey: ByteArray,
    ): ByteArray {
        val from = params.owner
        val coin = ChainTypeProxy().invoke(chain)
        val input = params.input
        val denom =
            if (input.assetId.type() == AssetSubtype.NATIVE) CosmosDenom.from(chain) else input.assetId.tokenId!!
        val message = when (input) {
            is ConfirmParams.TransferParams -> getTransferMessage(
                from = from,
                recipient = input.destination(),
                coin = coin,
                amount = getAmount(params.finalAmount, denom = denom)
            )
            is ConfirmParams.SwapParams,
            is ConfirmParams.TokenApprovalParams -> throw IllegalArgumentException()

        }
        return sign(params, privateKey, message)
    }

    private fun getTransferMessage(
        from: String,
        recipient: String,
        coin: CoinType,
        amount: Amount,
    ): List<Message> {
        val message =
            when (chain) {
                Chain.Thorchain -> Message(
                    thorchain_send_message =
                    Message.THORChainSend(
                        from_address = AnyAddress(from, coin).data.toByteString(),
                        to_address = AnyAddress(recipient, coin).data.toByteString(),
                        amounts = listOf(amount)
                    )
                )

                Chain.Cosmos,
                Chain.Celestia,
                Chain.Injective,
                Chain.Sei,
                Chain.Noble,
                Chain.Osmosis -> Message(
                    send_coins_message =
                    Message.Send(
                        from_address = from,
                        to_address = recipient,
                        amounts = listOf(amount),
                    )

                )

                else -> throw IllegalArgumentException()
            }
        return listOf(message)
    }


    private fun getAmount(amount: BigInteger, denom: String): Amount {
        return Amount(
            amount = amount.toString(),
            denom = denom
        )
    }

    private fun sign(
        input: SignerParams,
        privateKey: ByteArray,
        messages: List<Message>
    ): ByteArray {
        val meta = input.info as CosmosSignerPreloader.Info
        val fee = meta.fee as GasFee
        val feeAmount = fee.amount
        val gas = fee.limit.longValue() * messages.size
        val coin = ChainTypeProxy().invoke(chain)

        val cosmosFee = Fee(
            gas = gas,
            amounts = listOf(getAmount(feeAmount, CosmosDenom.from(chain)))
        )
        val memo = when (input.input.getTxType()) {
            TransactionType.Swap,
            TransactionType.Transfer,
            TransactionType.TokenApproval -> input.input.memo() ?: ""
            else -> ""
        }

        val signInput = SigningInput(
            mode = BroadcastMode.SYNC,
            signing_mode = SigningMode.Protobuf,
            chain_id = meta.chainId,
            account_number = meta.accountNumber,
            sequence =  meta.sequence,
            fee = cosmosFee,
            memo =  memo,
            private_key = privateKey.toByteString() ,
            messages = messages
        )
        val sign = AnySigner.sign(signInput, coin, SigningOutput.ADAPTER)
        return sign.serialized.toByteArray()
    }

    override fun maintainChain(): Chain = chain
}