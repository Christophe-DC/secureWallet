package com.cdcoding.network.client.solana

import com.cdcoding.common.utils.type
import com.cdcoding.model.Account
import com.cdcoding.model.ConfirmParams
import com.cdcoding.model.SignerParams
import com.cdcoding.network.client.SignerPreload
import com.cdcoding.network.model.JSONRpcRequest
import com.cdcoding.model.AssetSubtype
import com.cdcoding.model.Chain
import com.cdcoding.model.Fee
import com.cdcoding.model.SignerInputInfo
import com.cdcoding.network.util.NetworkError
import com.cdcoding.network.util.Result
import com.cdcoding.network.util.getOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext


class SolanaSignerPreloader(
    private val apiClient: SolanaApiClient,
) : SignerPreload {

    override suspend fun invoke(
        owner: Account,
        params: ConfirmParams,
    ): Result<SignerParams, NetworkError> = withContext(Dispatchers.IO) {
        val blockhashJob = async {
            apiClient.getBlockhash(JSONRpcRequest.create(SolanaMethod.GetLatestBlockhash, emptyList()))
                .getOrNull()?.result?.value?.blockhash
        }
        val feeJob = async { SolanaFee().invoke(apiClient) }
        val (fee, blockhash) = Pair(feeJob.await(), blockhashJob.await())

        if (blockhash.isNullOrEmpty()) {
            return@withContext Result.Error(NetworkError.UNKNOWN)
        }
        val info = when (params) {
            is ConfirmParams.TransferParams -> when (params.assetId.type()) {
                AssetSubtype.NATIVE -> {
                    Info(blockhash, "", null, fee)
                }
                AssetSubtype.TOKEN -> {
                    val (senderTokenAddress, recipientTokenAddress) = getTokenAccounts(params.assetId.tokenId!!, owner.address, params.to)

                    if (senderTokenAddress.isNullOrEmpty()) {
                        return@withContext Result.Error(NetworkError.UNKNOWN)
                    }
                    Info(
                        blockhash,
                        senderTokenAddress,
                        recipientTokenAddress,
                        if (recipientTokenAddress.isNullOrEmpty()) {
                            fee.withOptions("tokenAccountCreation")
                        } else {
                            fee
                        },
                    )
                }
            }
            is ConfirmParams.SwapParams -> Info(blockhash, "", null, fee)
            is ConfirmParams.TokenApprovalParams -> Info(blockhash, "", null, fee)
        }
        Result.Success(
            SignerParams(
                input = params,
                owner = owner.address,
                info = info,
            )
        )
    }

    private suspend fun getTokenAccounts(
        tokenId: String,
        senderAddress: String,
        recipientAddress: String,
    ): Pair<String?, String?> = withContext(Dispatchers.IO) {
        val senderTokenAddressJob = async {
            apiClient.getTokenAccountByOwner(senderAddress, tokenId)
                .getOrNull()?.result?.value?.firstOrNull()?.pubkey
        }
        val recipientTokenAddressJob = async {
            apiClient.getTokenAccountByOwner(recipientAddress, tokenId)
                .getOrNull()?.result?.value?.firstOrNull()?.pubkey
        }
        Pair(senderTokenAddressJob.await(), recipientTokenAddressJob.await())
    }

    override fun maintainChain(): Chain = Chain.Solana

    data class Info(
        val blockhash: String,
        val senderTokenAddress: String,
        val recipientTokenAddress: String?,
        val fee: Fee,
    ) : SignerInputInfo {
        override fun fee(): Fee = fee
    }
}