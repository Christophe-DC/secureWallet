package com.cdcoding.network.model

import kotlinx.serialization.Serializable

@Serializable
data class BalanceDto (
    val type: BalanceTypeDto,
    val value: String
)