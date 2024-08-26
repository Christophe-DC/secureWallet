package com.cdcoding.network.client

import com.cdcoding.model.TransactionChages

interface TransactionStatusClient : BlockchainClient {

    suspend fun getStatus(owner: String, txId: String): Result<TransactionChages>
}