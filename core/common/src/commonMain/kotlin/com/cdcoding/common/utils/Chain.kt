package com.cdcoding.common.utils

import com.cdcoding.model.Chain

val tokenAvailableChains = listOf(
    Chain.AvalancheC,
    Chain.Base,
    Chain.SmartChain,
    Chain.Arbitrum,
    Chain.Polygon,
    Chain.OpBNB,
    Chain.Fantom,
    Chain.Gnosis,
    Chain.Optimism,
    Chain.Manta,
    Chain.Blast,
    Chain.ZkSync,
    Chain.Linea,
    Chain.Mantle,
    Chain.Celo,
    Chain.Ethereum,
    Chain.Tron,
    Chain.Solana,
    Chain.Sui,
    Chain.Ton,
)


fun Chain.eip1559Support() = when (this) {
    Chain.OpBNB,
    Chain.Optimism,
    Chain.Base,
    Chain.AvalancheC,
    Chain.SmartChain,
    Chain.Polygon,
    Chain.Fantom,
    Chain.Gnosis,
    Chain.Manta,
    Chain.Blast,
    Chain.ZkSync,
    Chain.Linea,
    Chain.Mantle,
    Chain.Celo,
    Chain.Ethereum -> true
    Chain.Bitcoin,
    Chain.Litecoin,
    Chain.Solana,
    Chain.Thorchain,
    Chain.Cosmos,
    Chain.Osmosis,
    Chain.Sei,
    Chain.Arbitrum,
    Chain.Ton,
    Chain.Tron,
    Chain.Doge,
    Chain.Aptos,
    Chain.Sui,
    Chain.Celestia,
    Chain.Injective,
    Chain.Noble,
    Chain.Near,
    Chain.Xrp -> false
}

fun Chain.Companion.findByString(value: String): Chain? {
    return Chain.entries.firstOrNull{ it.string == value}
}

fun List<Chain>.filter(query: String): List<Chain> {
    return filter {
        val asset =  it.asset()
        asset.symbol.lowercase().startsWith(query) ||
        asset.name.lowercase().startsWith(query) ||
        it.string.lowercase().startsWith(query)
    }
}