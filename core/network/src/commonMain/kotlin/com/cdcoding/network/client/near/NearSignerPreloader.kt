package com.cdcoding.network.client.near

import com.cdcoding.common.utils.decodeHex
import com.cdcoding.model.Account
import com.cdcoding.model.AssetId
import com.cdcoding.model.Chain
import com.cdcoding.model.ConfirmParams
import com.cdcoding.model.Fee
import com.cdcoding.model.SignerInputInfo
import com.cdcoding.model.SignerParams
import com.cdcoding.network.client.SignerPreload
import com.cdcoding.network.model.JSONRpcRequest
import com.cdcoding.network.util.NetworkError
import com.cdcoding.network.util.Result
import com.cdcoding.network.util.getOrNull
import com.ionspin.kotlin.bignum.integer.BigInteger
import com.trustwallet.core.Base58
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext


class NearSignerPreloader(
    private val chain: Chain,
    private val apiClient: NearApiClient,
) : SignerPreload {
    override suspend fun invoke(owner: Account, params: ConfirmParams): Result<SignerParams, NetworkError> = withContext(
        Dispatchers.IO) {
        val getAccountJob = async {
            val publicKey = "ed25519:" + Base58.encodeNoCheck(owner.address.decodeHex())
            apiClient.accountAccessKey(
                JSONRpcRequest(
                    method = NearMethod.Query.value,
                    params = mapOf(
                        "request_type" to "view_access_key",
                        "finality" to "final",
                        "account_id" to owner.address,
                        "public_key" to publicKey,
                    )
                )
            ).getOrNull()
        }
        val blockJob = async {
            apiClient.latestBlock(
                JSONRpcRequest(
                    method = NearMethod.LatestBlock.value,
                    params = mapOf(
                        "finality" to "final",
                    )
                )
            ).getOrNull()
        }
        val gasPriceJob = async {
            apiClient.getGasPrice(
                JSONRpcRequest(
                    method = NearMethod.GasPrice.value,
                    params = listOf(null),
                )
            ).getOrNull()
        }
        val account = getAccountJob.await()?.result ?: throw IllegalStateException("Can't get account")
        val block = blockJob.await() ?: throw IllegalStateException("Can't get block")
        val gasPrice = gasPriceJob.await() ?: throw IllegalStateException("Can't get gas price")

        val fee = BigInteger.parseString("900000000000000000000")

        Result.Success(
            SignerParams(
                input = params,
                owner = owner.address,
                info = Info(
                    sequence = account.nonce + 1L,
                    block = block.result.header.hash,
                    fee = Fee(
                        feeAssetId = AssetId(chain),
                        amount = fee,
                    )
                )
            )
        )
    }

    override fun maintainChain(): Chain  = chain

    data class Info(
        val block: String,
        val sequence: Long,
        val fee: Fee,
    ) : SignerInputInfo {
        override fun fee(): Fee = fee
    }
}