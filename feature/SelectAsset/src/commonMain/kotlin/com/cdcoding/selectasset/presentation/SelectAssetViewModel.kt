package com.cdcoding.selectasset.presentation

import androidx.lifecycle.viewModelScope
import com.cdcoding.common.utils.CommonViewModel
import com.cdcoding.common.utils.getIconUrl
import com.cdcoding.common.utils.tokenAvailableChains
import com.cdcoding.data.repository.SessionRepository
import com.cdcoding.domain.GetAssetsByQueryUseCase
import com.cdcoding.model.AssetUIState
import com.cdcoding.model.PriceUIState
import com.ionspin.kotlin.bignum.integer.BigInteger
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SelectAssetViewModel(
    private val sessionRepository: SessionRepository,
    private val getAssetsByQueryUseCase: GetAssetsByQueryUseCase,
) : CommonViewModel<SelectAssetState, SelectAssetEvent, SelectAssetIntent>() {


    private val queryFlow = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private val assetsFlow =
        queryFlow
            .debounce(250)
            .distinctUntilChanged()
            .flatMapLatest { query ->
                val session = sessionRepository.getSession()
                    ?: throw IllegalArgumentException("Session doesn't found")

                setState { copy(isLoading = true) }

                getAssetsByQueryUseCase(session.wallet, query)
                    .map { assets ->
                        assets.sortedByDescending {
                            it.balances.available()
                                .convert(it.asset.decimals, it.price?.price?.price ?: 0.0)
                                .atomicValue
                        }
                    }
                    .flowOn(Dispatchers.IO)
            }

    override fun createInitialState(): SelectAssetState = SelectAssetState()

    override fun handleIntent(intent: SelectAssetIntent) {
        when (intent) {
            is SelectAssetIntent.OnQueryChanged -> {

                setState { copy(query = intent.value) }

                queryFlow.value = intent.value
            }
            else -> Unit
        }
    }

    init {
        viewModelScope.launch {
            assetsFlow.collect { assets ->
                val session = sessionRepository.getSession() ?: return@collect
                val availableAccounts = session.wallet.accounts.map { it.chain }
                val isAddAssetAvailable = tokenAvailableChains.any { it in availableAccounts }

                setState {
                    copy(
                        assets = assets.map {
                            AssetUIState(
                                id = it.asset.id,
                                name = it.asset.name,
                                icon = it.asset.getIconUrl(),
                                type = it.asset.type,
                                symbol = it.asset.symbol,
                                isZeroValue = it.balances.calcTotal().atomicValue == BigInteger.ZERO,
                                value = it.balances.calcTotal().format(it.asset.decimals, it.asset.symbol, 4),
                                price = PriceUIState.create(it.price?.price, session.currency),
                                fiat = if (it.price?.price == null || it.price!!.price.price == 0.0) {
                                    ""
                                } else {
                                    it.balances.calcTotal()
                                        .convert(it.asset.decimals, it.price!!.price.price)
                                        .format(0, session.currency.string, 2)
                                },
                                owner = it.owner.address,
                                metadata = it.metadata,
                            )
                        }.toImmutableList(),
                        isAddAssetAvailable = isAddAssetAvailable,
                        isLoading = false,
                    )
                }
            }
        }
    }

}