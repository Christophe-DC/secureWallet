package com.cdcoding.walletdetail.presentation

import androidx.lifecycle.viewModelScope
import com.cdcoding.common.utils.CommonViewModel
import com.cdcoding.common.utils.getIconUrl
import com.cdcoding.data.repository.AssetRepository
import com.cdcoding.domain.GetAssetsByWalletUseCase
import com.cdcoding.domain.GetSessionUseCase
import com.cdcoding.model.AssetInfo
import com.cdcoding.model.AssetUIState
import com.cdcoding.model.Chain
import com.cdcoding.model.Currency
import com.cdcoding.model.EVMChain
import com.cdcoding.model.Fiat
import com.cdcoding.model.PriceUIState
import com.cdcoding.model.Session
import com.cdcoding.model.TransactionExtended
import com.cdcoding.model.Wallet
import com.cdcoding.model.WalletSummary
import com.cdcoding.model.WalletType
import com.ionspin.kotlin.bignum.integer.BigInteger
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WalletDetailViewModel (
    private val getSessionUseCase: GetSessionUseCase,
    private val getAssetsByWalletUseCase: GetAssetsByWalletUseCase,
    private val assetRepository: AssetRepository
) : CommonViewModel<WalletDetailUIState, WalletDetailEffect, WalletDetailIntent>() {


    override fun createInitialState(): WalletDetailUIState {
        return WalletDetailUIState()
    }

    override fun handleIntent(intent: WalletDetailIntent) {
        when (intent) {
            is WalletDetailIntent.OnRefresh -> onRefresh()
        }
    }


    init {
        viewModelScope.launch {
            getSessionUseCase().collect { session ->
                if (session != null) {
                    getAssetsByWalletUseCase(session.wallet).collect { assetInfos ->
                        val swapEnabled = session.wallet.accounts.any { acc ->
                            EVMChain.entries.map { it.string }
                                .contains(acc.chain.string) || acc.chain == Chain.Solana
                        }
                        setState {
                            AssetsViewModelState(
                                session = session,
                                walletInfo = calcWalletInfo(session.wallet, assetInfos),
                                assets = handleAssets(session.currency, assetInfos),
                                currency = session.currency,
                                swapEnabled = swapEnabled,
                            ).toUIState()
                        }
                    }

                    onRefresh()

                }
            }
        }
    }


    fun onRefresh() {
        updateAssetData()
    }

    private fun updateAssetData() {
        val session = uiState.value.session ?: return
        setState { copy(isLoading = true) }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                assetRepository.syncTokens(session.wallet, session.currency)
                setState { copy(isLoading = false) }
            }
        }
    }

    private fun handleAssets(
        currency: Currency,
        assets: List<AssetInfo>
    ): ImmutableList<AssetUIState> {
        return assets
            .filter { asset ->
                asset.asset.assetMetaData?.isEnabled == true
            }
            .sortedByDescending {
                it.balances.calcTotal()
                    .convert(it.asset.decimals, it.price?.price?.price ?: 0.0).atomicValue
            }.map {
                val totalBalance = it.balances.calcTotal()
                AssetUIState(
                    id = it.asset.id,
                    name = it.asset.name,
                    icon = it.asset.getIconUrl(),
                    type = it.asset.type,
                    owner = it.owner.address,
                    value = totalBalance.format(it.asset.decimals, it.asset.symbol, 4),
                    isZeroValue = totalBalance.atomicValue == BigInteger.ZERO,
                    fiat = if (it.price == null || it.price?.price?.price == 0.0) {
                        ""
                    } else {
                        totalBalance.convert(it.asset.decimals, it.price?.price?.price ?: 0.0)
                            .format(0, currency.string, 2)
                    },
                    price = PriceUIState.create(it.price?.price, currency),
                    symbol = it.asset.symbol,
                )
            }.toImmutableList()
    }

    private fun calcWalletInfo(wallet: Wallet, assets: List<AssetInfo>): WalletSummary {
        val totals = assets.map {
            val current = it.balances
                .calcTotal()
                .convert(it.asset.decimals, it.price?.price?.price ?: 0.0)
                .atomicValue.doubleValue(true)
            val changed = current * ((it.price?.price?.priceChangePercentage24h ?: 0.0) / 100)
            Pair(current, changed)
        }.fold(Pair(0.0, 0.0)) { acc, pair ->
            Pair(acc.first + pair.first, acc.second + pair.second)
        }
        val changedPercentages = totals.second / (totals.first / 100.0)
        return WalletSummary(
            walletId = wallet.id,
            name = wallet.name,
            icon = if (wallet.type == WalletType.multicoin) {
                ""
            } else {
                wallet.accounts.firstOrNull()?.chain?.getIconUrl() ?: ""
            },
            type = wallet.type,
            totalValue = Fiat(totals.first),
            changedValue = Fiat(totals.second),
            changedPercentages = if (changedPercentages.isNaN()) {
                0.0
            } else {
                changedPercentages
            },
        )
    }
}


private data class AssetsViewModelState(
    val isLoading: Boolean = false,
    val session: Session? = null,
    val walletInfo: WalletSummary? = null,
    val currency: Currency = Currency.USD,
    val assets: ImmutableList<AssetUIState> = emptyList<AssetUIState>().toImmutableList(),
    val pendingTransactions: List<TransactionExtended> = emptyList(),
    val swapEnabled: Boolean = true,
    val error: String = "",
) {
    fun toUIState(): WalletDetailUIState {
        return WalletDetailUIState(
            isLoading = isLoading,
            session = session,
            walletInfo = WalletInfoUIState(
                name = walletInfo?.name ?: "",
                icon = walletInfo?.icon ?: "",
                totalValue = (walletInfo?.totalValue ?: Fiat(0.0)).format(0, currency.string, 2),
                changedValue = (walletInfo?.changedValue ?: Fiat(0.0)).format(
                    0,
                    currency.string,
                    2
                ),
                changedPercentages = PriceUIState.formatPercentage(
                    walletInfo?.changedPercentages ?: 0.0
                ),
                priceState = PriceUIState.getState(walletInfo?.changedPercentages ?: 0.0),
                type = walletInfo?.type ?: WalletType.view,
            ),
            assets = assets,
            swapEnabled = swapEnabled,
            pendingTransactions = pendingTransactions.toImmutableList()
        )
    }


}