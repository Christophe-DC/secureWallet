package com.cdcoding.network.client.ton

import com.cdcoding.common.utils.type
import com.cdcoding.model.AssetId
import com.cdcoding.model.AssetSubtype
import com.cdcoding.model.Fee
import com.cdcoding.network.util.getOrNull
import com.ionspin.kotlin.bignum.integer.BigInteger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class TonFee {
    suspend operator fun invoke(
        apiClient: TonApiClient,
        assetId: AssetId,
        destinationAddress: String,
        memo: String?,
    ): Fee = withContext(Dispatchers.IO) {
        when (assetId.type()) {
            AssetSubtype.NATIVE -> Fee(AssetId(assetId.chain), BigInteger.parseString("10000000"))
            AssetSubtype.TOKEN -> {
                val tokenId = assetId.tokenId!!
                val jetonAddress = jettonAddress(apiClient, tokenId, destinationAddress)
                    ?: throw Exception("can't get jetton address")
                val state = apiClient.addressState(jetonAddress).getOrNull()?.result == "active"
                val tokenAccountFee = if (state) {
                    if (memo.isNullOrEmpty()) {
                        BigInteger(100_000_000)
                    } else {
                        BigInteger(60_000_000) // 0.06
                    }
                } else {
                    BigInteger(300_000_000)
                }
                Fee(AssetId(assetId.chain), BigInteger.parseString("10000000"), options = mapOf(
                    tokenAccountCreationKey to tokenAccountFee
                )).withOptions(tokenAccountCreationKey)
            }
        }
    }
}