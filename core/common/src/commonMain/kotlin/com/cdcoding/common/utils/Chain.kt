package com.cdcoding.common.utils

import com.cdcoding.model.Chain
import com.ionspin.kotlin.bignum.integer.BigInteger

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


fun Chain.getReserveBalance(): BigInteger = when (this) {
    Chain.Xrp -> BigInteger(10_000_000)
    else -> BigInteger.ZERO
}

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

data class CaipChain(
    val namespace: String,
    val chainId: String,
    val displayName: String
)

val Chain.caip: CaipChain?
    get() = when (this) {

        Chain.Ethereum   -> CaipChain("eip155", "1", "Ethereum")
        Chain.Polygon    -> CaipChain("eip155", "137", "Polygon")
        Chain.Arbitrum   -> CaipChain("eip155", "42161", "Arbitrum")
        Chain.Optimism   -> CaipChain("eip155", "10", "Optimism")
        Chain.Base       -> CaipChain("eip155", "8453", "Base")
        Chain.AvalancheC -> CaipChain("eip155", "43114", "Avalanche")
        Chain.Fantom     -> CaipChain("eip155", "250", "Fantom")
        Chain.Gnosis     -> CaipChain("eip155", "100", "Gnosis")
        Chain.Celo       -> CaipChain("eip155", "42220", "Celo")
        Chain.Linea      -> CaipChain("eip155", "59144", "Linea")
        Chain.ZkSync     -> CaipChain("eip155", "324", "zkSync")
        Chain.Mantle     -> CaipChain("eip155", "5000", "Mantle")
        Chain.Blast      -> CaipChain("eip155", "81457", "Blast")
        Chain.OpBNB      -> CaipChain("eip155", "204", "opBNB")
        Chain.SmartChain -> CaipChain("eip155", "56", "BNB Smart Chain")

        Chain.Solana     -> CaipChain("solana", "5eykt4UsFv8P8NJdTREpY1vzqKqZKvdp", "Solana")
        Chain.Cosmos     -> CaipChain("cosmos", "cosmoshub-4", "Cosmos")
        Chain.Osmosis    -> CaipChain("cosmos", "osmosis-1", "Osmosis")
        Chain.Thorchain  -> CaipChain("cosmos", "thorchain-mainnet-v1", "Thorchain")
        Chain.Near       -> CaipChain("near", "mainnet", "Near")

        else -> null
    }