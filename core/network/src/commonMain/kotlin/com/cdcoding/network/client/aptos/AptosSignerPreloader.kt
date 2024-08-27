package com.cdcoding.network.client.aptos

import com.cdcoding.model.Account
import com.cdcoding.model.Chain
import com.cdcoding.model.ConfirmParams
import com.cdcoding.model.Fee
import com.cdcoding.model.SignerInputInfo
import com.cdcoding.model.SignerParams
import com.cdcoding.network.client.SignerPreload
import com.cdcoding.network.util.NetworkError
import com.cdcoding.network.util.getOrNull
import com.cdcoding.network.util.Result

class AptosSignerPreloader(
    private val chain: Chain,
    private val apiClient: AptosApiClient,
) : SignerPreload {
    override suspend fun invoke(
        owner: Account,
        params: ConfirmParams,
    ): Result<SignerParams, NetworkError> {
        val sequence =
            apiClient.getAccounts(owner.address).getOrNull()?.sequence_number?.toLong() ?: 0L
        val fee = AptosFee().invoke(chain, owner.address, apiClient)
        val input = SignerParams(
            input = params,
            owner = owner.address,
            info = Info(sequence = sequence, fee)
        )
        return Result.Success(input)
    }

    override fun maintainChain(): Chain = chain

    data class Info(
        val sequence: Long,
        val fee: Fee,
    ) : SignerInputInfo {
        override fun fee(): Fee = fee

    }
}