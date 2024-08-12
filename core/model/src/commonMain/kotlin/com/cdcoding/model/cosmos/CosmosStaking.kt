package com.cdcoding.model.cosmos

import kotlinx.serialization.Serializable

@Serializable
data class CosmosDelegationData (
	val validator_address: String
)

@Serializable
data class CosmosDelegation (
	val delegation: CosmosDelegationData,
	val balance: CosmosBalance
)

@Serializable
data class CosmosDelegations (
	val delegation_responses: List<CosmosDelegation>
)

@Serializable
data class CosmosUnboudingDelegationEntry (
	val completion_time: String,
	val creation_height: String,
	val balance: String
)

@Serializable
data class CosmosUnboundingDelegation (
	val validator_address: String,
	val entries: List<CosmosUnboudingDelegationEntry>
)

@Serializable
data class CosmosUnboundingDelegations (
	val unbonding_responses: List<CosmosUnboundingDelegation>
)

@Serializable
data class CosmosReward (
	val validator_address: String,
	val reward: List<CosmosBalance>
)

@Serializable
data class CosmosRewards (
	val rewards: List<CosmosReward>
)

@Serializable
data class CosmosValidatorMoniker (
	val moniker: String
)

@Serializable
data class CosmosValidatorCommissionRates (
	val rate: String
)

@Serializable
data class CosmosValidatorCommission (
	val commission_rates: CosmosValidatorCommissionRates
)

@Serializable
data class CosmosValidator (
	val operator_address: String,
	val jailed: Boolean,
	val status: String,
	val description: CosmosValidatorMoniker,
	val commission: CosmosValidatorCommission
)

@Serializable
data class CosmosValidators (
	val validators: List<CosmosValidator>
)

