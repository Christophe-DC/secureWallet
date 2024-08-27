package com.cdcoding.model

import com.ionspin.kotlin.bignum.integer.BigInteger


sealed class ConfirmParams(
    val assetId: AssetId,
    val amount: BigInteger = BigInteger.ZERO,
    val domainName: String? = null,
) {
    class TransferParams(
        assetId: AssetId,
        amount: BigInteger,
        val to: String,
        domainName: String? = null,
        val memo: String? = null,
        val isMaxAmount: Boolean = false,
    ) : ConfirmParams(assetId, amount, domainName) {

        override fun isMax(): Boolean {
            return isMaxAmount
        }

        override fun destination(): String {
            return to
        }

        override fun memo(): String? {
            return memo
        }
    }

    class TokenApprovalParams(
        assetId: AssetId,
        val approvalData: String,
        val provider: String,
    ) : ConfirmParams(assetId)

    class SwapParams(
        val fromAssetId: AssetId,
        val fromAmount: BigInteger,
        val toAssetId: AssetId,
        val toAmount: BigInteger,
        val swapData: String,
        val provider: String,
        val to: String,
        val value: String,
    ) : ConfirmParams(fromAssetId, fromAmount) {

        override fun destination(): String = to

    }


    fun getTxType() : TransactionType {
        return when (this) {
            is TransferParams  -> TransactionType.Transfer
            is TokenApprovalParams  -> TransactionType.TokenApproval
            is SwapParams  -> TransactionType.Swap
        }
    }

    open fun destination(): String = ""

    open fun memo(): String? = null

    open fun isMax(): Boolean = false

    override fun hashCode(): Int {
        return assetId.hashCode() +
                destination().hashCode() +
                memo().hashCode() +
                amount.hashCode() +
                isMax().hashCode()
    }

}