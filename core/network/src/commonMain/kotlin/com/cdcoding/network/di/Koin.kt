package com.cdcoding.network.di

import com.cdcoding.model.Chain
import com.cdcoding.network.client.BalanceClient
import com.cdcoding.network.client.BalancesRemoteSource
import com.cdcoding.network.client.BalancesRetrofitRemoteSource
import com.cdcoding.network.client.BroadcastProxy
import com.cdcoding.network.client.GemApiClient
import com.cdcoding.network.client.SignPreloaderProxy
import com.cdcoding.network.client.SignTransfer
import com.cdcoding.network.client.SignTransferProxy
import com.cdcoding.network.client.SignerPreload
import com.cdcoding.network.client.aptos.AptosApiClient
import com.cdcoding.network.client.aptos.AptosBalanceClient
import com.cdcoding.network.client.aptos.AptosBroadcastClient
import com.cdcoding.network.client.aptos.AptosSignClient
import com.cdcoding.network.client.aptos.AptosSignerPreloader
import com.cdcoding.network.client.bitcoin.BitcoinApiClient
import com.cdcoding.network.client.bitcoin.BitcoinBalanceClient
import com.cdcoding.network.client.bitcoin.BitcoinBroadcastClient
import com.cdcoding.network.client.bitcoin.BitcoinSignClient
import com.cdcoding.network.client.bitcoin.BitcoinSignerPreloader
import com.cdcoding.network.client.cosmo.CosmosApiClient
import com.cdcoding.network.client.cosmo.CosmosBalanceClient
import com.cdcoding.network.client.cosmo.CosmosBroadcastClient
import com.cdcoding.network.client.cosmo.CosmosSignClient
import com.cdcoding.network.client.cosmo.CosmosSignerPreloader
import com.cdcoding.network.client.ethereum.EvmApiClient
import com.cdcoding.network.client.ethereum.EvmBalanceClient
import com.cdcoding.network.client.ethereum.EvmBroadcastClient
import com.cdcoding.network.client.ethereum.EvmSignClient
import com.cdcoding.network.client.ethereum.EvmSignerPreloader
import com.cdcoding.network.client.near.NearApiClient
import com.cdcoding.network.client.near.NearBalanceClient
import com.cdcoding.network.client.near.NearBroadcastClient
import com.cdcoding.network.client.near.NearSignClient
import com.cdcoding.network.client.near.NearSignerPreloader
import com.cdcoding.network.client.solana.SolanaApiClient
import com.cdcoding.network.client.solana.SolanaBalanceClient
import com.cdcoding.network.client.solana.SolanaBroadcastClient
import com.cdcoding.network.client.solana.SolanaSignClient
import com.cdcoding.network.client.solana.SolanaSignerPreloader
import com.cdcoding.network.client.sui.SuiApiClient
import com.cdcoding.network.client.sui.SuiBalanceClient
import com.cdcoding.network.client.sui.SuiBroadcastClient
import com.cdcoding.network.client.sui.SuiSignClient
import com.cdcoding.network.client.ton.TonApiClient
import com.cdcoding.network.client.ton.TonBalanceClient
import com.cdcoding.network.client.ton.TonBroadcastClient
import com.cdcoding.network.client.ton.TonSignClient
import com.cdcoding.network.client.ton.TonSignerPreloader
import com.cdcoding.network.client.tron.TronApiClient
import com.cdcoding.network.client.tron.TronBalanceClient
import com.cdcoding.network.client.tron.TronBroadcastClient
import com.cdcoding.network.client.tron.TronSignClient
import com.cdcoding.network.client.tron.TronSignerPreloader
import com.cdcoding.network.client.xrp.XrpApiClient
import com.cdcoding.network.client.xrp.XrpBalanceClient
import com.cdcoding.network.client.xrp.XrpBroadcastClient
import com.cdcoding.network.client.xrp.XrpSignClient
import com.cdcoding.network.client.xrp.XrpSignerPreloader
import com.cdcoding.network.service.GemNameResolveService
import com.cdcoding.network.service.NameResolveService
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module


internal fun availableChains() = Chain.entries.toSet()

val networkModule = module {
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
    single { EvmApiClient(get()) }
    single { SolanaApiClient(get()) }
    single { CosmosApiClient(get()) }
    single { TonApiClient(get()) }
    single { TronApiClient(get()) }
    single { AptosApiClient(get()) }
    single { SuiApiClient(get()) }
    single { XrpApiClient(get()) }
    single { NearApiClient(get()) }
    single<NameResolveService> { GemNameResolveService(get()) }
    single<BalancesRemoteSource> { BalancesRetrofitRemoteSource(get()) }
    single<List<BalanceClient>> {
        availableChains().map {
            when (it) {
                Chain.Doge,
                Chain.Litecoin,
                Chain.Bitcoin,
                -> BitcoinBalanceClient(it, get())

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
                -> EvmBalanceClient(it, get())

                Chain.Solana -> SolanaBalanceClient(it, get())
                Chain.Thorchain,
                Chain.Osmosis,
                Chain.Celestia,
                Chain.Injective,
                Chain.Sei,
                Chain.Noble,
                Chain.Cosmos,
                -> CosmosBalanceClient(it, get())

                Chain.Ton -> TonBalanceClient(it, get())
                Chain.Tron -> TronBalanceClient(it, get())
                Chain.Aptos -> AptosBalanceClient(it, get())
                Chain.Sui -> SuiBalanceClient(it, get())
                Chain.Xrp -> XrpBalanceClient(it, get())
                Chain.Near -> NearBalanceClient(it, get())
            }
        }
    }

    single<SignerPreload> {
        SignPreloaderProxy(
            availableChains().map { chain ->
                when (chain) {
                    Chain.Doge,
                    Chain.Litecoin,
                    Chain.Bitcoin,
                    -> BitcoinSignerPreloader(chain, get())

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
                    -> EvmSignerPreloader(chain, get())

                    Chain.Solana -> SolanaSignerPreloader(get())
                    Chain.Thorchain,
                    Chain.Osmosis,
                    Chain.Celestia,
                    Chain.Injective,
                    Chain.Sei,
                    Chain.Noble,
                    Chain.Cosmos,
                    -> CosmosSignerPreloader(chain, get())

                    Chain.Ton -> TonSignerPreloader(get())
                    Chain.Tron -> TronSignerPreloader(get())
                    Chain.Aptos -> AptosSignerPreloader(chain, get())
                    Chain.Sui,
                    Chain.Xrp,
                    -> XrpSignerPreloader(chain, get())

                    Chain.Near -> NearSignerPreloader(chain, get())
                }
            }
        )
    }

    single<SignTransfer> {
        SignTransferProxy(
            clients = listOf(
                SolanaSignClient(),
                TronSignClient(),
                BitcoinSignClient(Chain.Bitcoin),
                BitcoinSignClient(Chain.Doge),
                BitcoinSignClient(Chain.Litecoin),
                TonSignClient(),
                EvmSignClient(Chain.Ethereum),
                EvmSignClient(Chain.Fantom),
                EvmSignClient(Chain.Gnosis),
                EvmSignClient(Chain.AvalancheC),
                EvmSignClient(Chain.Base),
                EvmSignClient(Chain.SmartChain),
                EvmSignClient(Chain.Arbitrum),
                EvmSignClient(Chain.Polygon),
                EvmSignClient(Chain.OpBNB),
                EvmSignClient(Chain.Manta),
                EvmSignClient(Chain.Blast),
                EvmSignClient(Chain.ZkSync),
                EvmSignClient(Chain.Linea),
                EvmSignClient(Chain.Mantle),
                EvmSignClient(Chain.Celo),
                CosmosSignClient(Chain.Cosmos),
                CosmosSignClient(Chain.Osmosis),
                CosmosSignClient(Chain.Thorchain),
                CosmosSignClient(Chain.Celestia),
                CosmosSignClient(Chain.Injective),
                CosmosSignClient(Chain.Sei),
                CosmosSignClient(Chain.Noble),
                AptosSignClient(Chain.Aptos),
                SuiSignClient(Chain.Sui),
                XrpSignClient(Chain.Xrp),
                NearSignClient(Chain.Near),
            )
        )
    }

    single<BroadcastProxy> {
        BroadcastProxy(
            availableChains().map { chain ->
                when (chain) {
                    Chain.Doge,
                    Chain.Litecoin,
                    Chain.Bitcoin,
                    -> BitcoinBroadcastClient(chain, get())

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
                    -> EvmBroadcastClient(chain, get())

                    Chain.Solana -> SolanaBroadcastClient(get())
                    Chain.Thorchain,
                    Chain.Osmosis,
                    Chain.Celestia,
                    Chain.Injective,
                    Chain.Sei,
                    Chain.Noble,
                    Chain.Cosmos,
                    -> CosmosBroadcastClient(chain, get())

                    Chain.Ton -> TonBroadcastClient(get())
                    Chain.Tron -> TronBroadcastClient(get())
                    Chain.Aptos -> AptosBroadcastClient(chain, get())
                    Chain.Sui -> SuiBroadcastClient(chain, get())
                    Chain.Xrp -> XrpBroadcastClient(chain, get())
                    Chain.Near -> NearBroadcastClient(chain, get())
                }
            }
        )
    }

}