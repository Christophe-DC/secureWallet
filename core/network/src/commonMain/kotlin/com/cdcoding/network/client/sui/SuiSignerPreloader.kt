package com.cdcoding.network.client.sui

import com.cdcoding.common.utils.type
import com.cdcoding.model.Chain
import com.cdcoding.model.Account
import com.cdcoding.model.ConfirmParams
import com.cdcoding.model.Fee
import com.cdcoding.model.SignerInputInfo
import com.cdcoding.model.SignerParams
import com.cdcoding.network.util.NetworkError
import com.cdcoding.network.client.SignerPreload
import com.cdcoding.network.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class SuiSignerPreloader(
    private val chain: Chain,
    private val apiClient: SuiApiClient,
) : SignerPreload {

    private val coinId = "0x2::sui::SUI"

    override suspend fun invoke(owner: Account, params: ConfirmParams): Result<SignerParams, NetworkError> {

        return Result.Error(NetworkError.UNKNOWN)
    }

    override fun maintainChain(): Chain = chain

    data class Info(
        val messageBytes: String,
        val fee: Fee,
    ) : SignerInputInfo {
        override fun fee(): Fee = fee
    }

}