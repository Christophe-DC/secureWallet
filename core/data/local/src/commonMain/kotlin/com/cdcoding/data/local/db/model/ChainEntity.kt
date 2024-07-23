package com.cdcoding.data.local.db.model

import com.cdcoding.model.Chain

enum class ChainEntity(val string: String) {
    Bitcoin("bitcoin"),
    Litecoin("litecoin"),
    Ethereum("ethereum"),
    SmartChain("smartchain"),
    Solana("solana"),
    Polygon("polygon"),
    Thorchain("thorchain"),
    Cosmos("cosmos"),
    Osmosis("osmosis"),
    Arbitrum("arbitrum"),
    Ton("ton"),
    Tron("tron"),
    Doge("doge"),
    Optimism("optimism"),
    Aptos("aptos"),
    Base("base"),
    AvalancheC("avalanchec"),
    Sui("sui"),
    Xrp("xrp"),
    OpBNB("opbnb"),
    Fantom("fantom"),
    Gnosis("gnosis"),
    Celestia("celestia"),
    Injective("injective"),
    Sei("sei"),
    Manta("manta"),
    Blast("blast"),
    Noble("noble"),
    ZkSync("zksync"),
    Linea("linea"),
    Mantle("mantle"),
    Celo("celo"),
    Near("near");

    companion object {
        operator fun invoke(string: String): ChainEntity? {
            return fromRaw(string)
        }

        private fun fromRaw(string: String): ChainEntity? {
            return entries.find { it.string == string }
        }
    }

}


fun ChainEntity.asExternal(): Chain {
    return when (this) {
        ChainEntity.Bitcoin -> Chain.Bitcoin
        ChainEntity.Litecoin -> Chain.Litecoin
        ChainEntity.Ethereum -> Chain.Ethereum
        ChainEntity.SmartChain -> Chain.SmartChain
        ChainEntity.Solana -> Chain.Solana
        ChainEntity.Polygon -> Chain.Polygon
        ChainEntity.Thorchain -> Chain.Thorchain
        ChainEntity.Cosmos -> Chain.Cosmos
        ChainEntity.Osmosis -> Chain.Osmosis
        ChainEntity.Arbitrum -> Chain.Arbitrum
        ChainEntity.Ton -> Chain.Ton
        ChainEntity.Tron -> Chain.Tron
        ChainEntity.Doge -> Chain.Doge
        ChainEntity.Optimism -> Chain.Optimism
        ChainEntity.Aptos -> Chain.Aptos
        ChainEntity.Base -> Chain.Base
        ChainEntity.AvalancheC -> Chain.AvalancheC
        ChainEntity.Sui -> Chain.Sui
        ChainEntity.Xrp -> Chain.Xrp
        ChainEntity.OpBNB -> Chain.OpBNB
        ChainEntity.Fantom -> Chain.Fantom
        ChainEntity.Gnosis -> Chain.Gnosis
        ChainEntity.Celestia -> Chain.Celestia
        ChainEntity.Injective -> Chain.Injective
        ChainEntity.Sei -> Chain.Sei
        ChainEntity.Manta -> Chain.Manta
        ChainEntity.Blast -> Chain.Blast
        ChainEntity.Noble -> Chain.Noble
        ChainEntity.ZkSync -> Chain.ZkSync
        ChainEntity.Linea -> Chain.Linea
        ChainEntity.Mantle -> Chain.Mantle
        ChainEntity.Celo -> Chain.Celo
        ChainEntity.Near -> Chain.Near
    }
}

fun Chain.asEntity(): ChainEntity {
    return when (this) {
        Chain.Bitcoin -> ChainEntity.Bitcoin
        Chain.Litecoin -> ChainEntity.Litecoin
        Chain.Ethereum -> ChainEntity.Ethereum
        Chain.SmartChain -> ChainEntity.SmartChain
        Chain.Solana -> ChainEntity.Solana
        Chain.Polygon -> ChainEntity.Polygon
        Chain.Thorchain -> ChainEntity.Thorchain
        Chain.Cosmos -> ChainEntity.Cosmos
        Chain.Osmosis -> ChainEntity.Osmosis
        Chain.Arbitrum -> ChainEntity.Arbitrum
        Chain.Ton -> ChainEntity.Ton
        Chain.Tron -> ChainEntity.Tron
        Chain.Doge -> ChainEntity.Doge
        Chain.Optimism -> ChainEntity.Optimism
        Chain.Aptos -> ChainEntity.Aptos
        Chain.Base -> ChainEntity.Base
        Chain.AvalancheC -> ChainEntity.AvalancheC
        Chain.Sui -> ChainEntity.Sui
        Chain.Xrp -> ChainEntity.Xrp
        Chain.OpBNB -> ChainEntity.OpBNB
        Chain.Fantom -> ChainEntity.Fantom
        Chain.Gnosis -> ChainEntity.Gnosis
        Chain.Celestia -> ChainEntity.Celestia
        Chain.Injective -> ChainEntity.Injective
        Chain.Sei -> ChainEntity.Sei
        Chain.Manta -> ChainEntity.Manta
        Chain.Blast -> ChainEntity.Blast
        Chain.Noble -> ChainEntity.Noble
        Chain.ZkSync -> ChainEntity.ZkSync
        Chain.Linea -> ChainEntity.Linea
        Chain.Mantle -> ChainEntity.Mantle
        Chain.Celo -> ChainEntity.Celo
        Chain.Near -> ChainEntity.Near
    }
}

