package com.cdcoding.network.client.cosmo

import com.cdcoding.model.AssetId
import com.cdcoding.model.Chain
import com.cdcoding.model.DelegationBase
import com.cdcoding.model.DelegationState
import com.cdcoding.model.DelegationValidator
import com.cdcoding.model.cosmos.CosmosDenom
import com.cdcoding.model.cosmos.from
import com.cdcoding.network.client.StakeClient
import com.cdcoding.network.util.getOrNull
import com.ionspin.kotlin.bignum.integer.BigInteger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

class CosmosStakeClient(
    private val chain: Chain,
    private val CosmosClient: CosmosApiClient,
) : StakeClient {

    override suspend fun getValidators(apr: Double): List<DelegationValidator> {
        return CosmosClient.getValidators().getOrNull()?.validators?.map {
            val commission = it.commission.commission_rates.rate.toDouble()
            val isActive = !it.jailed && it.status == "BOND_STATUS_BONDED"
            DelegationValidator(
                chain = chain,
                id = it.operator_address,
                name = it.description.moniker,
                isActive = isActive,
                commision = commission,
                apr = if (isActive) apr - (apr * commission) else 0.0,
            )
        } ?: emptyList()
    }

    override suspend fun getStakeDelegations(address: String, apr: Double): List<DelegationBase> = withContext(Dispatchers.IO) {
        val getDelegations = async { CosmosClient.getDelegations(address).getOrNull()?.delegation_responses }
        val getUnboundingDelegations = async { CosmosClient.getUndelegations(address).getOrNull()?.unbonding_responses }
        val getRewards = async {
            CosmosClient.getRewards(address).getOrNull()?.rewards
                ?.associateBy { it.validator_address }
                ?.mapValues { entry ->
                    entry.value.reward
                        .filter { it.denom == CosmosDenom.from(chain) }
                        .mapNotNull {
                            try {
                                BigInteger.parseString(it.amount.split(".")[0])
                            } catch (err: Throwable) {
                                BigInteger.ZERO
                            }
                        }
                        .reduceOrNull { acc, value -> acc + value }
                        ?.toString()
                } ?: emptyMap()
        }
        val delegations = getDelegations.await()
        val undelegations = getUnboundingDelegations.await()
        val rewards = getRewards.await()

        val baseDelegations = delegations?.map {
            DelegationBase(
                assetId = AssetId(chain),
                state = DelegationState.Active,
                balance = it.balance.amount,
                completionDate = null,
                delegationId = "",
                validatorId = it.delegation.validator_address,
                rewards = rewards[it.delegation.validator_address] ?: "0",
                shares = "",
            )
        } ?: emptyList()

        val baseUndelegations: List<DelegationBase> = undelegations?.map { undelegation ->
            undelegation.entries.map { entry ->
                DelegationBase(
                    assetId = AssetId(chain),
                    state = DelegationState.Pending,
                    balance = entry.balance,
                    completionDate = LocalDateTime.parse(entry.completion_time).toInstant(TimeZone.UTC).epochSeconds,  // SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").parse(entry.completion_time)?.time, //2024-02-28T08:12:56.376120563Z
                    delegationId = entry.creation_height,
                    validatorId = undelegation.validator_address,
                    rewards = rewards[undelegation.validator_address] ?: "0",
                    shares = "",
                )
            }
        }?.flatten() ?: emptyList()

        (baseDelegations + baseUndelegations)
    }

    override fun maintainChain(): Chain = chain
}