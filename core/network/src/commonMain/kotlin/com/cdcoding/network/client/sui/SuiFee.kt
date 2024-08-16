package com.cdcoding.network.client.sui

import com.cdcoding.model.Account
import com.cdcoding.model.AssetId
import com.cdcoding.model.Fee
import com.cdcoding.network.model.JSONRpcRequest
import com.cdcoding.network.util.getOrNull
import com.ionspin.kotlin.bignum.integer.toBigInteger


class SuiFee {
    suspend operator fun invoke(
        ApiClient: SuiApiClient,
        account: Account,
        data: String,
    ): Fee {
        val chain = account.chain
        val gasUsed = ApiClient.dryRun(JSONRpcRequest.create(SuiMethod.DryRun, listOf(data)))
            .getOrNull()?.result?.effects?.gasUsed ?: throw Exception()
        val computationCost = gasUsed.computationCost.toBigInteger()
        val storageCost = gasUsed.storageCost.toBigInteger()
        val storageRebate = gasUsed.storageRebate.toBigInteger()
        val fee = computationCost + storageCost - storageRebate
        return Fee(feeAssetId = AssetId(chain), amount = fee.abs())
    }
}