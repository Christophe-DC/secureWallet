package com.cdcoding.model.solana

import kotlinx.serialization.Serializable

@Serializable
data class SolanaTokenAccount (
	val pubkey: String
)

@Serializable
data class SolanaTokenAccountResult<T> (
	val account: T,
	val pubkey: String
)

@Serializable
data class SolanaStakeAccountDataParsedInfoStakeDelegation (
	val voter: String,
	val stake: String,
	val activationEpoch: String,
	val deactivationEpoch: String
)

@Serializable
data class SolanaStakeAccountDataParsedInfoStake (
	val delegation: SolanaStakeAccountDataParsedInfoStakeDelegation
)

@Serializable
data class SolanaStakeAccountDataParsedInfoMeta (
	val rentExemptReserve: String
)

@Serializable
data class SolanaStakeAccountDataParsedInfo (
	val stake: SolanaStakeAccountDataParsedInfoStake,
	val meta: SolanaStakeAccountDataParsedInfoMeta
)

@Serializable
data class SolanaStakeAccountDataParsed (
	val info: SolanaStakeAccountDataParsedInfo
)

@Serializable
data class SolanaStakeAccountData (
	val parsed: SolanaStakeAccountDataParsed
)

@Serializable
data class SolanaStakeAccount (
	val lamports: Long,
	val space: Int,
	val data: SolanaStakeAccountData
)

