package com.cdcoding.receiveasset.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.cdcoding.common.utils.CommonViewModel
import com.cdcoding.common.utils.getAccount
import com.cdcoding.data.repository.AssetRepository
import com.cdcoding.data.repository.SessionRepository
import com.cdcoding.model.AssetId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

class ReceiveAssetViewModel(
    savedStateHandle: SavedStateHandle,
    private val sessionRepository: SessionRepository,
    private val assetRepository: AssetRepository,
) : CommonViewModel<ReceiveAssetState, ReceiveAssetEvent, ReceiveAssetIntent>() {

    override fun createInitialState(): ReceiveAssetState {
        return ReceiveAssetState()
    }

    override fun handleIntent(intent: ReceiveAssetIntent) {}

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val assetId: AssetId? = savedStateHandle["assetId"]
            if (assetId == null) {
                setState { copy(errorMessage = "Missing assetId", isLoading = false) }
                return@launch
            }
            val session = sessionRepository.getSession()
            if (session == null) {
                setState { copy(errorMessage = "Can't find active wallet", isLoading = false) }
                return@launch
            }

            val account = session.wallet.getAccount(assetId.chain)
            if (account == null) {
                setState { copy(errorMessage = "Asset doesn't find", isLoading = false) }
                return@launch
            }

            val assetInfo =
                assetRepository.getById(session.wallet.accounts, assetId).getOrNull()?.firstOrNull()
            if (assetInfo == null) {
                setState { copy(errorMessage = "Asset doesn't find", isLoading = false) }
            } else {
                setState {
                    copy(
                        walletName = session.wallet.name,
                        address = account.address,
                        assetTitle = assetInfo.asset.name,
                        assetSymbol = assetInfo.asset.symbol,
                        chain = account.chain,
                        isLoading = false
                    )
                }
            }
        }
    }

}