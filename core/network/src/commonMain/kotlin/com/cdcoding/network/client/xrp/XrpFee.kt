package com.cdcoding.network.client.xrp

import com.cdcoding.model.AssetId
import com.cdcoding.model.Chain
import com.cdcoding.model.Fee
import com.cdcoding.network.util.getOrNull
import com.ionspin.kotlin.bignum.integer.toBigInteger

class XrpFee {
    suspend operator fun invoke(
        chain: Chain,
        apiClient: XrpApiClient,
    ): Fee {
        val median = apiClient.fee().getOrNull()?.result?.drops?.median_fee?.toBigInteger() ?: throw Exception("Unknown fee")
        return Fee(feeAssetId = AssetId(chain), amount = median)
    }
}