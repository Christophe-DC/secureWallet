package com.cdcoding.network.client.ton

import com.cdcoding.common.utils.type
import com.cdcoding.model.Account
import com.cdcoding.model.AssetSubtype
import com.cdcoding.model.ConfirmParams
import com.cdcoding.model.SignerParams
import com.cdcoding.model.Chain
import com.cdcoding.model.Fee
import com.cdcoding.model.SignerInputInfo
import com.cdcoding.network.client.SignerPreload
import com.cdcoding.network.util.NetworkError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import com.cdcoding.network.util.Result
import com.cdcoding.network.util.getOrNull
import com.cdcoding.network.util.map

internal const val tokenAccountCreationKey: String = "tokenAccountCreation"

class TonSignerPreloader(
    private val apiClient: TonApiClient,
) : SignerPreload {

    override suspend fun invoke(owner: Account, params: ConfirmParams): Result<SignerParams, NetworkError> = withContext(Dispatchers.IO) {
        when (params.assetId.type()) {
            AssetSubtype.NATIVE -> coinSign(owner, params)
            AssetSubtype.TOKEN -> tokenSign(owner, params)
        }

    }

    override fun maintainChain(): Chain = Chain.Ton

    private suspend fun coinSign(owner: Account, params: ConfirmParams):  Result<SignerParams, NetworkError> {
        val fee = TonFee().invoke(apiClient, params.assetId, params.destination(), params.memo())
        return apiClient.walletInfo(owner.address).map {
            SignerParams(
                input = params,
                owner = owner.address,
                info = Info(sequence = it.result.seqno ?: 0, fee = fee)
            )
        }
    }

    private suspend fun tokenSign(owner: Account, params: ConfirmParams): Result<SignerParams, NetworkError> = withContext(Dispatchers.IO) {
        val getWalletInfo = async { apiClient.walletInfo(owner.address).getOrNull() }
        val getJettonAddress = async { jettonAddress(apiClient, params.assetId.tokenId!!, owner.address) }
        val feeJob = async { TonFee().invoke(apiClient, params.assetId, params.destination(), params.memo()) }
        val walletInfo = getWalletInfo.await()
            ?: return@withContext Result.Error(NetworkError.UNKNOWN)
        val jettonAddress = getJettonAddress.await() ?: return@withContext Result.Error(NetworkError.UNKNOWN)
        val fee = feeJob.await()

        val signerParams = SignerParams(
            input = params,
            owner = owner.address,
            info = Info(
                sequence = walletInfo.result.seqno ?: 0,
                jettonAddress = jettonAddress,
                fee = fee,
            )
        )

        Result.Success(signerParams)
    }

    data class Info(
        val sequence: Int,
        val jettonAddress: String? = null,
        val fee: Fee,
    ) : SignerInputInfo {
        override fun fee(): Fee = fee
    }
}