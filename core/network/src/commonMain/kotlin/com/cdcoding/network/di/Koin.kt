package com.cdcoding.network.di

import com.cdcoding.model.Chain
import com.cdcoding.network.client.BalanceClient
import com.cdcoding.network.client.BalancesRemoteSource
import com.cdcoding.network.client.BalancesRetrofitRemoteSource
import com.cdcoding.network.client.GemApiClient
import com.cdcoding.network.client.GetTokenClient
import com.cdcoding.network.client.bitcoin.BitcoinApiClient
import com.cdcoding.network.client.bitcoin.BitcoinBalanceClient
import com.cdcoding.network.service.GemNameResolveService
import com.cdcoding.network.service.NameResolveService
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.dsl.module

//expect fun platformModule(): Module

internal fun availableChains() = Chain.entries.toSet()

val networkModule = module {
    // includes(platformModule())
    single {
        HttpClient {
            install(Logging) {
                level = LogLevel.ALL
            }
            install(ContentNegotiation) {
                json(
                    json = Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
        }
    }
    single<GemApiClient> { GemApiClient(get()) }

    single { BitcoinApiClient(get()) }
    single<NameResolveService> { GemNameResolveService(get()) }
    single<BalancesRemoteSource> { BalancesRetrofitRemoteSource(get()) }
    single<List<BalanceClient>> {
        availableChains().map {
             when (it) {
                Chain.Doge,
                Chain.Litecoin,
                Chain.Bitcoin -> BitcoinBalanceClient(it, get())

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
                Chain.Ethereum -> BitcoinBalanceClient(it, get()) // EvmBalanceClient(it, rpcClients.getClient(it))

                Chain.Solana -> BitcoinBalanceClient(it, get()) //SolanaBalanceClient(it, rpcClients.getClient(Chain.Solana))
                Chain.Thorchain,
                Chain.Osmosis,
                Chain.Celestia,
                Chain.Injective,
                Chain.Sei,
                Chain.Noble,
                Chain.Cosmos -> BitcoinBalanceClient(it, get())// CosmosBalanceClient(it, rpcClients.getClient(it))

                Chain.Ton -> BitcoinBalanceClient(it, get()) //TonBalanceClient(it, rpcClients.getClient(Chain.Ton))
                Chain.Tron -> BitcoinBalanceClient(it, get()) // TronBalanceClient(it, rpcClients.getClient(Chain.Tron))
                Chain.Aptos -> BitcoinBalanceClient(it, get()) // AptosBalanceClient(it, rpcClients.getClient(it))
                Chain.Sui -> BitcoinBalanceClient(it, get()) //SuiBalanceClient(it, rpcClients.getClient(it))
                Chain.Xrp -> BitcoinBalanceClient(it, get()) //XrpBalanceClient(it, rpcClients.getClient(it))
                Chain.Near -> BitcoinBalanceClient(it, get()) //NearBalanceClient(it, rpcClients.getClient(it))
            }
        }
    }

    /*single<List<GetTokenClient>> {
        availableChains().map {
            when (it) {
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
                Chain.Ethereum -> EvmGetTokenClient(it, rpcClients.getClient(it))
                Chain.Tron -> TronGetTokenClient(it, rpcClients.getClient(Chain.Tron))
                Chain.Solana -> SolanaTokenClient(it, rpcClients.getClient(Chain.Solana))
                Chain.Sui -> SuiGetTokenClient(it, rpcClients.getClient(it))
                Chain.Ton -> TonGetTokenClient(it, rpcClients.getClient(it))
                Chain.Doge,
                Chain.Litecoin,
                Chain.Bitcoin,
                Chain.Thorchain,
                Chain.Osmosis,
                Chain.Celestia,
                Chain.Injective,
                Chain.Sei,
                Chain.Noble,
                Chain.Cosmos,
                Chain.Aptos,
                Chain.Xrp,
                Chain.Near -> null
            }
        }
    }*/

}