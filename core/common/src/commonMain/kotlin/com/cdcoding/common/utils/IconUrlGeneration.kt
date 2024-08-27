package com.cdcoding.common.utils

import com.cdcoding.model.Asset
import com.cdcoding.model.AssetId
import com.cdcoding.model.AssetSubtype
import com.cdcoding.model.Chain


fun Asset.chain(): Chain = id.chain

fun Asset.getIconUrl(): String = id.getIconUrl()

fun Asset.getSupportIconUrl(): String = if (id.type() == AssetSubtype.NATIVE) "" else id.chain.getIconUrl()

fun AssetId.getIconUrl(): String {
    return when {
        tokenId.isNullOrEmpty() -> chain.getIconUrl()
        else -> "https://assets.gemwallet.com/blockchains/${chain.string}/assets/${tokenId}/logo.png"
    }
}


fun Chain.getIconUrl(): String =  "https://assets.gemwallet.com/blockchains/${string}/logo.png"