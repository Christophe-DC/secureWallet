package com.cdcoding.network.client.aptos

import com.cdcoding.model.AssetId
import com.cdcoding.model.Chain
import com.cdcoding.model.Fee
import com.cdcoding.model.GasFee
import com.cdcoding.network.util.getOrNull
import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.toBigInteger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

internal class AptosFee {
    suspend operator fun invoke(
        chain: Chain,
        ownerAddress: String,
        apiClient: AptosApiClient,
    ): Fee = withContext(Dispatchers.IO) {
        val gasPriceJob =
            async {
                apiClient.getFeePrice().getOrNull()?.prioritized_gas_estimate?.toBigInteger() ?:  throw Exception("Fee price is null")
            }
        val sequenceJob = async {
            try {
                apiClient.getAccounts(ownerAddress).getOrNull()?.sequence_number?.toLong()
            } catch (err: Throwable) {
                null
            }
        }
        val (gasPrice, sequence) = Pair(gasPriceJob.await(), sequenceJob.await())
        GasFee(
            feeAssetId = AssetId(chain),
            maxGasPrice = gasPrice,
            limit = BigInteger.fromInt(if (sequence == null) 676 else 6)
        )
    }
}