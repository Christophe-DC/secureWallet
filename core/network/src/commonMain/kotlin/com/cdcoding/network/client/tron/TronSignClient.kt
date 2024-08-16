package com.cdcoding.network.client.tron

import com.cdcoding.common.utils.type
import com.cdcoding.model.AssetSubtype
import com.cdcoding.model.Chain
import com.cdcoding.model.SignerParams
import com.cdcoding.network.client.SignClient
import com.ionspin.kotlin.bignum.integer.BigInteger
import com.trustwallet.core.AnySigner
import com.trustwallet.core.CoinType
import com.trustwallet.core.sign
import com.trustwallet.core.tron.BlockHeader
import com.trustwallet.core.tron.SigningInput
import com.trustwallet.core.tron.SigningOutput
import com.trustwallet.core.tron.Transaction
import com.trustwallet.core.tron.TransferContract
import com.trustwallet.core.tron.TransferTRC20Contract
import io.ktor.utils.io.core.toByteArray
import okio.ByteString.Companion.toByteString

class TronSignClient : SignClient {
    override suspend fun signTransfer(
        params: SignerParams,
        privateKey: ByteArray
    ): ByteArray {
        val blockInfo = params.info as TronSignerPreloader.Info
        val transaction = when (params.input.assetId.type()) {
            AssetSubtype.NATIVE -> Transaction(
                block_header = BlockHeader(
                    number = blockInfo.number,
                    parent_hash = blockInfo.parentHash.toByteArray().toByteString(),
                    timestamp = blockInfo.timestamp,
                    version = blockInfo.version.toInt(),
                    witness_address = blockInfo.witnessAddress.toByteArray().toByteString(),
                    tx_trie_root = blockInfo.txTrieRoot.toByteArray().toByteString()
                ),
                transfer = getTransferContract(
                    params.finalAmount,
                    params.owner,
                    params.input.destination()
                ),
                expiration = blockInfo.timestamp + 10 * 3600000,
                timestamp = blockInfo.timestamp,
                fee_limit = blockInfo.fee.amount.longValue()
            )

            AssetSubtype.TOKEN ->
                Transaction(
                    block_header = BlockHeader(
                        number = blockInfo.number,
                        parent_hash = blockInfo.parentHash.toByteArray().toByteString(),
                        timestamp = blockInfo.timestamp,
                        version = blockInfo.version.toInt(),
                        witness_address = blockInfo.witnessAddress.toByteArray().toByteString(),
                        tx_trie_root = blockInfo.txTrieRoot.toByteArray().toByteString()
                    ),
                    transfer_trc20_contract = getTransferTRC20Contract(
                        params.input.assetId.tokenId!!,
                        params.finalAmount,
                        params.owner,
                        params.input.destination()
                    ),
                    expiration = blockInfo.timestamp + 10 * 3600000,
                    timestamp = blockInfo.timestamp,
                    fee_limit = blockInfo.fee.amount.longValue()
                )

            else -> throw IllegalArgumentException("Unsupported type")
        }

        val signInput = SigningInput(
            transaction = transaction,
            private_key = privateKey.toByteString()
        )
        val signingOutput = AnySigner.sign(signInput, CoinType.Tron, SigningOutput.ADAPTER)
        return signingOutput.json.toByteArray()
    }

    private fun getTransferContract(value: BigInteger, ownerAddress: String, recipient: String) =
        TransferContract(
            amount = value.longValue(),
            owner_address = ownerAddress,
            to_address = recipient
        )

    private fun getTransferTRC20Contract(
        tokenId: String,
        value: BigInteger,
        ownerAddress: String,
        recipient: String
    ) = TransferTRC20Contract(
        contract_address = tokenId,
        owner_address = ownerAddress,
        to_address = recipient,
        amount = value.toByteArray().toByteString(),
    )

    override fun maintainChain(): Chain = Chain.Tron
}