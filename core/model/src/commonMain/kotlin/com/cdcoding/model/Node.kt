
package com.cdcoding.model

enum class NodeStatus(val string: String) {
	Active("active"),
	Inactive("inactive"),
}

data class Node (
	val url: String,
	val status: NodeStatus,
	val priority: Int
)

data class ChainNode (
	val chain: String,
	val node: Node
)

data class ChainNodes (
	val chain: String,
	val nodes: List<Node>
)

data class NodesResponse (
	val version: Int,
	val nodes: List<ChainNodes>
)

