package com.cdcoding.network.client.bitcoin

import com.cdcoding.model.Account
import com.cdcoding.model.Chain
import com.cdcoding.model.ConfirmParams
import com.cdcoding.model.Fee
import com.cdcoding.model.SignerInputInfo
import com.cdcoding.model.SignerParams
import com.cdcoding.model.bitcoin.BitcoinUTXO
import com.cdcoding.network.client.SignerPreload
import com.cdcoding.network.util.NetworkError
import com.cdcoding.network.util.Result
import com.cdcoding.network.util.getOrNull
import com.cdcoding.network.util.map


class BitcoinSignerPreloader(
    private val chain: Chain,
    private val apiClient: BitcoinApiClient,
) : SignerPreload {

    override suspend fun invoke(
        owner: Account,
        params: ConfirmParams,
    ): Result<SignerParams, NetworkError> {
        return apiClient.getUTXO(owner.extendedPublicKey!!).map {
            val fee = BitcoinFee().invoke(apiClient, it, owner, params.destination(), params.amount)
            SignerParams(
                input = params,
                owner = owner.address,
                info = Info(it, fee)
            )
        }
    }

    override fun maintainChain(): Chain = chain

    data class Info(
        val utxo: List<BitcoinUTXO>,
        val fee: Fee,
    ) : SignerInputInfo {
        override fun fee(): Fee = fee
    }
}