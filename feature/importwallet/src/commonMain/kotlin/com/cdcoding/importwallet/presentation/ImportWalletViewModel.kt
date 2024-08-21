package com.cdcoding.importwallet.presentation

import androidx.lifecycle.viewModelScope
import com.cdcoding.common.utils.CommonViewModel
import com.cdcoding.data.repository.ImportError
import com.cdcoding.data.repository.WalletRepository
import com.cdcoding.model.NameRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ImportWalletViewModel(
    private val walletRepository: WalletRepository,
) : CommonViewModel<ImportWalletState, ImportWalletEvent, ImportWalletIntent>() {

    override fun createInitialState(): ImportWalletState {
        return ImportWalletState()
    }

    override fun handleIntent(intent: ImportWalletIntent) {
        when (intent) {
            is ImportWalletIntent.OnImport -> {
                import(intent.name, intent.generatedName, intent.data, intent.nameRecord, intent.onImported)
            }
        }
    }

    init {
        viewModelScope.launch {
            val generatedNameIndex = walletRepository.getNextWalletNumber()
            setState { copy(generatedNameIndex = generatedNameIndex) }
        }
    }

    fun import(
        name: String,
        generatedName:String,
        data: String,
        nameRecord: NameRecord?,
        onImported: () -> Unit
    ) = viewModelScope.launch {
        setState { copy(loading = true) }
        withContext(Dispatchers.IO) {
            walletRepository.importWallet(
                walletName = name.ifEmpty { generatedName },
                data = if (nameRecord?.address.isNullOrEmpty()) data.trim() else nameRecord!!.address,
            )
        }.onFailure { err ->
            setState { copy(dataError = err as? ImportError ?: ImportError.CreateError("Unknown error") , loading = false) }
        }.onSuccess {
            setState { copy(dataError = null, loading = false) }
            onImported()
        }
    }

}