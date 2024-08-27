package com.cdcoding.network.client.solana

import com.cdcoding.model.AssetId
import com.cdcoding.model.Chain
import com.cdcoding.model.Fee
import com.cdcoding.network.model.JSONRpcRequest
import com.cdcoding.network.util.getOrNull
import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.toBigInteger


class SolanaFee {

    private val baseFee = BigInteger(50_000)
    private val priorityMultiplayer = 5L
    private val tokenAccountSize = 165

    suspend operator fun invoke(apiClient: SolanaApiClient): Fee {
        val request = JSONRpcRequest.create(SolanaMethod.GetPriorityFee, listOf<String>())
        val fees = apiClient.getPriorityFees(request).getOrNull()?.result ?: throw Exception()
        val priorityFee = fees.map { it.prioritizationFee }.fold(0) { acc, i -> acc + i } / fees.size

        val tokenAccountCreation = apiClient.rentExemption(JSONRpcRequest(id = 1, method = SolanaMethod.RentExemption.value, params = listOf(tokenAccountSize)))
            .getOrNull()?.result?.toBigInteger() ?: throw Exception("Can't get fee")
        val fee = baseFee + BigInteger(priorityFee * priorityMultiplayer)
        return Fee(
            feeAssetId = AssetId(Chain.Solana),
            amount = fee,
            options = mapOf("tokenAccountCreation" to tokenAccountCreation)
        )
    }
}