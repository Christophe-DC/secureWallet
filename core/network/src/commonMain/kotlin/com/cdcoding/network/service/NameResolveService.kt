package com.cdcoding.network.service

import com.cdcoding.model.Chain
import com.cdcoding.model.NameRecord
import com.cdcoding.network.client.GemApiClient
import com.cdcoding.network.util.getOrNull


interface NameResolveService {
    suspend fun resolve(name: String, chain: Chain): NameRecord?
}

class GemNameResolveService(
    private val client: GemApiClient,
) : NameResolveService {
    override suspend fun resolve(name: String, chain: Chain): NameRecord? {
        return client.resolve(name, chain.string).getOrNull()
    }
}