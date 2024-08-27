package com.cdcoding.network.client

import com.cdcoding.model.Account
import com.cdcoding.model.ConfirmParams
import com.cdcoding.model.SignerParams
import com.cdcoding.network.util.NetworkError
import com.cdcoding.network.util.Result

interface SignerPreload : BlockchainClient {
    suspend operator fun invoke(
        owner: Account,
        params: ConfirmParams,
    ): Result<SignerParams, NetworkError>
}