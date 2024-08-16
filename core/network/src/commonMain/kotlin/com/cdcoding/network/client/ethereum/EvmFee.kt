package com.cdcoding.network.client.ethereum

import com.cdcoding.common.utils.hexToBigInteger
import com.cdcoding.common.utils.type
import com.cdcoding.model.AssetId
import com.cdcoding.model.ConfirmParams
import com.cdcoding.model.EVMChain
import com.cdcoding.model.Fee
import com.cdcoding.model.TransactionType
import com.ionspin.kotlin.bignum.integer.BigInteger
import com.trustwallet.core.CoinType
import kotlinx.coroutines.Dispatchers
import com.cdcoding.model.AssetSubtype
import com.cdcoding.model.GasFee
import com.cdcoding.network.model.JSONRpcRequest
import com.cdcoding.network.util.getOrNull
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext


class EvmFee {
    suspend operator fun invoke(
        apiClient: EvmApiClient,
        params: ConfirmParams,
        chainId: BigInteger,
        nonce: BigInteger,
        gasLimit: BigInteger,
        coinType: CoinType,
    ): Fee = withContext(Dispatchers.IO) {
        val assetId = params.assetId
        val isMaxAmount = params.isMax()
        val feeAssetId = AssetId(assetId.chain)

        if (EVMChain.isOpStack(params.assetId.chain)) {
            return@withContext OptimismGasOracle().invoke(
                params = params,
                chainId = chainId,
                nonce = nonce,
                gasLimit = gasLimit,
                coin = coinType,
                apiClient = apiClient,
            )
        }
        val (baseFee, priorityFee) = getBasePriorityFee(apiClient)

        val maxGasPrice = baseFee.plus(priorityFee)
        val minerFee = when (params.getTxType()) {
            TransactionType.Transfer -> if (assetId.type() == AssetSubtype.NATIVE && isMaxAmount) maxGasPrice else priorityFee
            TransactionType.Swap,
            TransactionType.TokenApproval -> priorityFee
        }
        GasFee(feeAssetId = feeAssetId, limit = gasLimit, maxGasPrice = maxGasPrice, minerFee = minerFee)
    }

    companion object {
        internal suspend fun getBasePriorityFee(
            apiClient: EvmApiClient
        ): Pair<BigInteger, BigInteger> {
            val feeHistory = apiClient.getFeeHistory(
                JSONRpcRequest.create(EvmMethod.GetFeeHistory, listOf("10", "latest", listOf(25)))
            ).getOrNull()?.result ?: throw Exception("Unable to calculate base fee") // TODO: Handle errors
            val reward = feeHistory.reward
                .mapNotNull { it.firstOrNull() }
                .mapNotNull { it.hexToBigInteger() }
                .maxByOrNull { it }
                ?: throw Exception("Unable to calculate priority fee")
            val baseFee = feeHistory.baseFeePerGas.mapNotNull{ it.hexToBigInteger() }.maxByOrNull { it }
                ?: throw Exception("Unable to calculate base fee")
            // Default 0.01 gwei
            val priorityFee = if (reward == BigInteger.ZERO) BigInteger(10_000_000) else reward

            return Pair(baseFee, priorityFee)
        }
    }
}