package com.cdcoding.selectwallet.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.cdcoding.common.utils.CommonViewModel
import com.cdcoding.common.utils.getIconUrl
import com.cdcoding.data.repository.AssetRepository
import com.cdcoding.data.repository.SessionRepository
import com.cdcoding.data.repository.WalletRepository
import com.cdcoding.domain.DeleteWalletUseCase
import com.cdcoding.domain.GetSessionUseCase
import com.cdcoding.model.AssetId
import com.cdcoding.model.Chain
import com.cdcoding.model.NameRecord
import com.cdcoding.model.WalletType
import com.cdcoding.wallet.validator.ValidateAddressOperator
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SelectWalletViewModel(
    private val walletRepository: WalletRepository,
    private val sessionRepository: SessionRepository,
    private val deleteWalletUseCase: DeleteWalletUseCase,
    private val getSessionUseCase: GetSessionUseCase,
) : CommonViewModel<SelectWalletUIState, SelectWalletEvent, SelectWalletIntent>() {


    override fun createInitialState(): SelectWalletUIState {
        return SelectWalletUIState()
    }

    override fun handleIntent(intent: SelectWalletIntent) {
        when (intent) {
            is SelectWalletIntent.OnWalletSelected -> handleSelectWallet(intent.walletId)
            is SelectWalletIntent.OnDeleteWallet -> handleDeleteWallet(intent.walletId, intent.onBoard)
        }
    }

    init {
        initialize()
    }

    private fun initialize() {
        viewModelScope.launch {
            walletRepository.getAllWallets().collect { wallets ->
                setState {
                    copy(
                        wallets = wallets.map {
                            WalletItemUIState(
                                id = it.id,
                                name = it.name,
                                icon = if (it.accounts.size > 1) {
                                    ""
                                } else {
                                    it.accounts.firstOrNull()?.chain?.getIconUrl() ?: ""
                                }
                            )
                        }
                    )
                }
            }
        }
        viewModelScope.launch {
            getSessionUseCase().collect { session ->
                setState {
                    copy(
                        currentWalletId = session?.wallet?.id ?: ""
                    )
                }
            }
        }
    }

    fun handleSelectWallet(walletId: String) {
        viewModelScope.launch {
            sessionRepository.setSession(walletId)
        }
    }

    fun handleDeleteWallet(walletId: String, onBoard: () -> Unit) = viewModelScope.launch {
        deleteWalletUseCase(walletId, onBoard)
    }

}