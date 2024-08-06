package com.cdcoding.wallet.operator

import com.cdcoding.model.Chain
import com.trustwallet.core.CoinType

class ChainTypeProxy {
    operator fun invoke(chain: Chain): CoinType = when (chain) {
        Chain.Bitcoin -> CoinType.Bitcoin
        Chain.Litecoin -> CoinType.Litecoin
        Chain.Ethereum -> CoinType.Ethereum
        Chain.SmartChain -> CoinType.SmartChain
        Chain.Solana -> CoinType.Solana
        Chain.Polygon -> CoinType.Polygon
        Chain.Thorchain -> CoinType.THORChain
        Chain.Cosmos -> CoinType.Cosmos
        Chain.Osmosis -> CoinType.Osmosis
        Chain.Arbitrum -> CoinType.Arbitrum
        Chain.Ton -> CoinType.TON
        Chain.Tron -> CoinType.Tron
        Chain.Doge -> CoinType.Dogecoin
        Chain.Optimism -> CoinType.Optimism
        Chain.AvalancheC -> CoinType.AvalancheCChain
        Chain.Base -> CoinType.Base
        Chain.Aptos -> CoinType.Aptos
        Chain.Sui -> CoinType.Sui
        Chain.Xrp -> CoinType.XRP
        Chain.OpBNB -> CoinType.OpBNB
        Chain.Fantom -> CoinType.Fantom
        Chain.Gnosis -> CoinType.Osmosis
        Chain.Celestia -> CoinType.Tia
        Chain.Injective -> CoinType.NativeInjective
        Chain.Sei -> CoinType.Sei
        Chain.Manta -> CoinType.MantaPacific
        Chain.Blast -> CoinType.Blast
        Chain.Noble -> CoinType.Noble
        Chain.ZkSync -> CoinType.Zksync
        Chain.Linea -> CoinType.Linea
        Chain.Mantle -> CoinType.Mantle
        Chain.Celo -> CoinType.Celo
        Chain.Near -> CoinType.NEAR
    }
}