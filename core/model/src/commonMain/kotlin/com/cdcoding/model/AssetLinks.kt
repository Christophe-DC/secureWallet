package com.cdcoding.model

import kotlinx.serialization.Serializable

@Serializable
data class AssetLinks (
    val homepage: String? = null,
    val explorer: String? = null,
    val twitter: String? = null,
    val telegram: String? = null,
    val github: String? = null,
    val youtube: String? = null,
    val facebook: String? = null,
    val reddit: String? = null,
    val coingecko: String? = null,
    val coinmarketcap: String? = null,
    val discord: String? = null
)