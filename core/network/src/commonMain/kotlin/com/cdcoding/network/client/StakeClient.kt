package com.cdcoding.network.client

import com.cdcoding.model.DelegationBase
import com.cdcoding.model.DelegationValidator

interface StakeClient : BlockchainClient {
    suspend fun getValidators(apr: Double): List<DelegationValidator>

    suspend fun getStakeDelegations(address: String, apr: Double): List<DelegationBase>
}