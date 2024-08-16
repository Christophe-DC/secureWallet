package com.cdcoding.model.cosmos

import kotlinx.serialization.Serializable

@Serializable
data class CosmosHeader (
	val chain_id: String
)

@Serializable
data class CosmosBlock (
	val header: CosmosHeader
)

@Serializable
data class CosmosBlockResponse (
	val block: CosmosBlock
)

@Serializable
data class CosmosNodeInfo (
	val network: String
)

@Serializable
data class CosmosNodeInfoResponse (
	val default_node_info: CosmosNodeInfo
)

