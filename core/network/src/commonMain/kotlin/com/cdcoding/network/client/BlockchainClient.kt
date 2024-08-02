package com.cdcoding.network.client

import com.cdcoding.model.Chain


interface BlockchainClient {
    fun isMaintain(chain: Chain): Boolean = chain == maintainChain()

    fun maintainChain(): Chain
}