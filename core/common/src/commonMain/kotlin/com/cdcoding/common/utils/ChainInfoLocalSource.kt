package com.cdcoding.common.utils

import com.cdcoding.model.Chain


interface ChainInfoLocalSource {
    suspend fun getAll(): List<Chain>

    companion object {
        val exclude = listOf(Chain.Celo)
    }
}