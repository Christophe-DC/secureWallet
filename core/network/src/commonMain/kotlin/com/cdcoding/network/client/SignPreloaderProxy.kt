package com.cdcoding.network.client

import com.cdcoding.model.Account
import com.cdcoding.model.Chain
import com.cdcoding.model.ConfirmParams
import com.cdcoding.model.SignerParams
import com.cdcoding.network.util.NetworkError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import com.cdcoding.network.util.Result

class SignPreloaderProxy(
    private val preloaders: List<SignerPreload>
) : SignerPreload {
    override suspend fun invoke(
        owner: Account,
        params: ConfirmParams,
    ): Result<SignerParams, NetworkError> = withContext(Dispatchers.IO) {
        try {
            preloaders.firstOrNull { it.isMaintain(owner.chain) }
                ?.invoke(owner = owner, params = params) ?: Result.Error(NetworkError.UNKNOWN)

        } catch (err: Throwable) {
            Result.Error(NetworkError.UNKNOWN)
        }
    }

    override fun maintainChain(): Chain {
        throw Exception()
    }
}