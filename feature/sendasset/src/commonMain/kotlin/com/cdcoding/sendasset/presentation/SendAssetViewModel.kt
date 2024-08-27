package com.cdcoding.sendasset.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.cdcoding.common.utils.CommonViewModel
import com.cdcoding.data.repository.AssetRepository
import com.cdcoding.data.repository.SessionRepository
import com.cdcoding.model.AssetId
import com.cdcoding.model.Chain
import com.cdcoding.model.NameRecord
import com.cdcoding.wallet.validator.ValidateAddressOperator
import kotlinx.coroutines.launch

class SendAssetViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val sessionRepository: SessionRepository,
    private val assetRepository: AssetRepository,
    private val validateAddressOperator: ValidateAddressOperator,
) : CommonViewModel<SendAssetUIState, SendAssetEvent, SendAssetIntent>() {


    override fun createInitialState(): SendAssetUIState {
        return SendAssetUIState()
    }

    override fun handleIntent(intent: SendAssetIntent) {
        when (intent) {
            is SendAssetIntent.OnValueChange -> onValueChange(intent.input)
            is SendAssetIntent.OnQrScanner -> onQrScanner(intent.scanType)
            SendAssetIntent.OnScanCanceled -> onScanCanceled()
            is SendAssetIntent.SetQrData -> setQrData(intent.data)
            is SendAssetIntent.OnNext -> onNext(intent.input, intent.nameRecord, intent.memo, intent.onRecipientComplete)
        }
    }

    init {
        initialize()
    }

    private fun initialize() {
        val assetId: AssetId? = savedStateHandle["assetId"]
        if (assetId == null) {
            setState { copy(screen = SendAssetStateScreen.Fatal(error = "Missing assetId")) }
            return
        }
        val wallet = sessionRepository.getSession()?.wallet
        if (wallet == null) {
            setState { copy(screen = SendAssetStateScreen.Fatal(error = "Select asset")) }
            return
        }

        setState {
            copy(
                address = savedStateHandle["destinationAddress"] ?: "",
                addressDomain = savedStateHandle["addressDomain"] ?: "",
                memo = savedStateHandle["memo"] ?: "",
                screen = SendAssetStateScreen.Loading
            )
        }

        viewModelScope.launch {
            assetRepository.getById(wallet.accounts, assetId)
                .fold(
                    onSuccess = {
                        setState {
                            copy(
                                assetInfo = it.first(),
                                screen = SendAssetStateScreen.Idle
                            )
                        }
                    }
                ) {
                    setState {
                        copy(
                            screen = SendAssetStateScreen.Fatal(
                                error = it.message ?: "Asset doesn't found"
                            )
                        )
                    }
                }
        }
    }


    private fun onValueChange(input: String) {
        setState { copy(address = input) }
    }

    private fun onQrScanner(scanType: ScanType) {
        setState { copy(screen = SendAssetStateScreen.ScanQr(scanType)) }
    }

    private fun onScanCanceled() {
        setState { copy(screen = SendAssetStateScreen.Idle) }
    }

    private fun setQrData(data: String) {
        if (uiState.value.screen is SendAssetStateScreen.ScanQr) {
            when ((uiState.value.screen as SendAssetStateScreen.ScanQr).scanType) {
                ScanType.Address -> setState { copy(address = data) }
                ScanType.Memo -> setState { copy(memo = data) }
            }
        }
        setState { copy(screen = SendAssetStateScreen.Idle) }
    }

    private fun onNext(
        input: String,
        nameRecord: NameRecord?,
        memo: String,
        onRecipientComplete: () -> Unit
    ) {
        val asset = uiState.value.assetInfo?.asset ?: return
        val (address, addressDomain) = if (nameRecord?.name == input) Pair(
            nameRecord.address,
            nameRecord.name
        ) else Pair(input, "")
        val recipientError = validateRecipient(asset.id.chain, address)
        setState {
            copy(
                addressDomain = addressDomain,
                addressError = recipientError,
            )
        }
        if (recipientError == RecipientFormError.None) {
            onRecipientComplete(
               /* assetId = asset.id,
                destinationAddress = address,
                addressDomain = addressDomain,
                memo = memo,
                delegationId = "",
                validatorId = "",
                txType = TransactionType.Transfer,*/
            )
        }
    }

    private fun validateRecipient(chain: Chain, recipient: String): RecipientFormError {
        return if (validateAddressOperator(recipient, chain).getOrNull() != true) {
            RecipientFormError.IncorrectAddress
        } else {
            RecipientFormError.None
        }
    }

}