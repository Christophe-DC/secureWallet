package com.cdcoding.network.client.xrp

import com.cdcoding.model.Account
import com.cdcoding.model.Chain
import com.cdcoding.model.ConfirmParams
import com.cdcoding.model.Fee
import com.cdcoding.model.SignerInputInfo
import com.cdcoding.model.SignerParams
import com.cdcoding.network.client.SignerPreload
import com.cdcoding.network.util.NetworkError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import com.cdcoding.network.util.Result
import com.cdcoding.network.util.map
import kotlinx.coroutines.IO

class XrpSignerPreloader(
    private val chain: Chain,
    private val apiClient: XrpApiClient,
) : SignerPreload {
    override suspend fun invoke(owner: Account, params: ConfirmParams): Result<SignerParams, NetworkError> = withContext(Dispatchers.IO) {
        val (sequenceJob, feeJob) = Pair (
            async {
                apiClient.account(owner.address)
            },
            async { XrpFee().invoke(chain, apiClient) }
        )
        val (sequenceResult, fee) = Pair(sequenceJob.await(), feeJob.await())
        sequenceResult.map {
            val sequence = it.result.account_data.Sequence
            SignerParams(
                input = params,
                owner = owner.address,
                info = Info(
                    sequence = sequence,
                    fee = fee,
                )
            )

        }
    }

    override fun maintainChain(): Chain = chain

    data class Info(
        val sequence: Int,
        val fee: Fee,
    ) : SignerInputInfo {
        override fun fee(): Fee = fee
    }
}