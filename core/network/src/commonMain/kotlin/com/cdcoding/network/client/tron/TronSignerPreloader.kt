package com.cdcoding.network.client.tron

import com.cdcoding.common.utils.type
import com.cdcoding.model.Account
import com.cdcoding.model.Chain
import com.cdcoding.model.ConfirmParams
import com.cdcoding.model.Fee
import com.cdcoding.model.SignerInputInfo
import com.cdcoding.model.SignerParams
import com.cdcoding.network.client.SignerPreload
import com.cdcoding.network.util.NetworkError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import com.cdcoding.network.util.Result
import com.cdcoding.network.util.map

class TronSignerPreloader(
    private val apiClient: TronApiClient,
) : SignerPreload {
    override suspend fun invoke(owner: Account, params: ConfirmParams): Result<SignerParams, NetworkError> = withContext(Dispatchers.IO) {
        val feeJob = async {
            try {
                TronFee().invoke(
                    apiClient = apiClient,
                    account = owner,
                    recipientAddress = params.destination(),
                    value = params.amount,
                    contractAddress = params.assetId.tokenId,
                    type = params.assetId.type(),
                )
            } catch (err: Throwable) {
                null
            }
        }
        val nowBlockJob = async { apiClient.nowBlock() }

        val fee = feeJob.await() ?: return@withContext Result.Error(NetworkError.UNKNOWN)
        val nowBlock = nowBlockJob.await()

        nowBlock.map {
            SignerParams(
                input = params,
                owner = owner.address,
                info = Info(
                    number = it.block_header.raw_data.number,
                    version = it.block_header.raw_data.version,
                    txTrieRoot = it.block_header.raw_data.txTrieRoot,
                    witnessAddress = it.block_header.raw_data.witness_address,
                    parentHash = it.block_header.raw_data.parentHash,
                    timestamp = it.block_header.raw_data.timestamp,
                    fee = fee,
                )
            )
        }
    }

    override fun maintainChain(): Chain = Chain.Tron

    data class Info(
        val number: Long,
        val version: Long,
        val txTrieRoot: String,
        val witnessAddress: String,
        val parentHash: String,
        val timestamp: Long,
        val fee: Fee,
    ) : SignerInputInfo {
        override fun fee(): Fee = fee
    }
}