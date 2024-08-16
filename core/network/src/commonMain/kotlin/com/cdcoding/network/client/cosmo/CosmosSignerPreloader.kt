package com.cdcoding.network.client.cosmo

import com.cdcoding.model.Account
import com.cdcoding.model.Chain
import com.cdcoding.model.ConfirmParams
import com.cdcoding.model.Fee
import com.cdcoding.model.SignerInputInfo
import com.cdcoding.model.SignerParams
import com.cdcoding.network.client.SignerPreload
import com.cdcoding.network.util.NetworkError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import com.cdcoding.network.util.Result
import com.cdcoding.network.util.getOrNull
import kotlinx.coroutines.async


class CosmosSignerPreloader(
    private val chain: Chain,
    private val apiClient: CosmosApiClient,
) : SignerPreload {
    override suspend fun invoke(
        owner: Account,
        params: ConfirmParams,
    ): Result<SignerParams, NetworkError> = withContext(Dispatchers.IO) {
        val accountJob = async {
            if (chain == Chain.Injective) {
                apiClient.getInjectiveAccountData(owner.address).getOrNull()?.account?.base_account
            } else {
                apiClient.getAccountData(owner.address).getOrNull()?.account
            }
        }
        val nodeInfoJob = async { apiClient.getNodeInfo() }
        val feeJob = async { CosmosFee(txType = params.getTxType()).invoke(chain) }

        val (account, nodeInfo, fee) = Triple(
            accountJob.await(),
            nodeInfoJob.await().getOrNull(),
            feeJob.await()
        )
        return@withContext if (account != null && nodeInfo != null) {
            Result.Success(
                SignerParams(
                    input = params,
                    owner = owner.address,
                    info = Info(
                        chainId = nodeInfo.block.header.chain_id,
                        accountNumber = account.account_number.toLong(),
                        sequence = account.sequence.toLong(),
                        fee = fee,
                    )
                )
            )
        } else {
            Result.Error(NetworkError.UNKNOWN)
        }
    }

    override fun maintainChain(): Chain = chain

    data class Info(
        val chainId: String,
        val accountNumber: Long,
        val sequence: Long,
        val fee: Fee,
    ) : SignerInputInfo {
        override fun fee(): Fee = fee
    }
}