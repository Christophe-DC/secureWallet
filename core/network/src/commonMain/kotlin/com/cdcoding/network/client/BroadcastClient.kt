package com.cdcoding.network.client

import com.cdcoding.model.Account
import com.cdcoding.model.TransactionType


interface BroadcastClient : BlockchainClient {
    suspend fun send(account: Account, signedMessage: ByteArray, type: TransactionType): Result<String>
}