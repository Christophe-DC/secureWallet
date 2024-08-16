package com.cdcoding.confirm.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.cdcoding.common.utils.CommonViewModel
import com.cdcoding.common.utils.asset
import com.cdcoding.common.utils.getAddressEllipsisText
import com.cdcoding.common.utils.getIconUrl
import com.cdcoding.core.designsystem.table.CellEntity
import com.cdcoding.core.resource.Res
import com.cdcoding.core.resource.swap_provider
import com.cdcoding.core.resource.transaction_recipient
import com.cdcoding.core.resource.transfer_from
import com.cdcoding.core.resource.transfer_memo
import com.cdcoding.core.resource.transfer_network
import com.cdcoding.core.resource.transfer_network_fee
import com.cdcoding.data.repository.AssetRepository
import com.cdcoding.data.repository.SessionRepository
import com.cdcoding.data.repository.TransactionRepository
import com.cdcoding.datastore.password.PasswordStore
import com.cdcoding.model.Asset
import com.cdcoding.model.AssetInfo
import com.cdcoding.model.ConfirmParams
import com.cdcoding.model.Crypto
import com.cdcoding.model.Fee
import com.cdcoding.model.TransactionDirection
import com.cdcoding.model.TransactionState
import com.cdcoding.model.TransactionSwapMetadata
import com.cdcoding.network.client.BroadcastProxy
import com.cdcoding.network.client.SignTransfer
import com.cdcoding.network.client.SignerPreload
import com.cdcoding.network.util.getOrNull
import com.cdcoding.wallet.operator.LoadPrivateKeyOperator
import com.ionspin.kotlin.bignum.integer.BigInteger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ConfirmViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val sessionRepository: SessionRepository,
    private val assetRepository: AssetRepository,
    private val signerPreload: SignerPreload,
    private val passwordStore: PasswordStore,
    private val loadPrivateKeyOperator: LoadPrivateKeyOperator,
    private val signTransfer: SignTransfer,
    private val broadcastProxy: BroadcastProxy,
    private val transactionRepository: TransactionRepository,
) : CommonViewModel<ConfirmUIState, ConfirmEvent, ConfirmIntent>() {

    override fun createInitialState(): ConfirmUIState {
        return ConfirmUIState()
    }

    override fun handleIntent(intent: ConfirmIntent) {
        when (intent) {
            is ConfirmIntent.OnSend -> onSend()
        }
    }

    init {
        viewModelScope.launch {
            initialize()
        }
    }

    fun initialize() = viewModelScope.launch(Dispatchers.IO) {

        val params: ConfirmParams? = savedStateHandle["confirmParams"]
        if (params == null) {
            setState { copy(screen = ConfirmStateScreen.Fatal(error = "Missing params")) }
            return@launch
        }

        val session = sessionRepository.getSession() ?: return@launch
        val wallet = session.wallet
        val assetInfo =
            assetRepository.getById(wallet.accounts, params.assetId).getOrNull()?.firstOrNull()
        if (assetInfo == null) {
            setState { copy(screen = ConfirmStateScreen.Fatal(error = "Asset doesn't find")) }
            return@launch
        }
        val signerParams = withContext(Dispatchers.IO) {
            signerPreload(
                owner = assetInfo.owner,
                params = params,
            ).getOrNull()
        }
        if (signerParams?.info == null) {
            setState { copy(screen = ConfirmStateScreen.Fatal(error = "Not transaction info")) }
            return@launch
        }
        val fee = signerParams.info.fee()
        val feeAssetInfo =
            assetRepository.getById(wallet.accounts, fee.feeAssetId).getOrNull()?.firstOrNull()
        if (feeAssetInfo == null) {
            setState { copy(screen = ConfirmStateScreen.Fatal(error = "Fee asset doesn't find")) }
            return@launch
        }
        val finalParams = when {
            params.isMax() && params.assetId == feeAssetInfo.asset.id -> signerParams.copy(
                finalAmount = params.amount - fee.amount
            )

            else -> signerParams.copy(finalAmount = params.amount)
        }
        val balance = getBalance(assetInfo = assetInfo, params = params)
        val error = validateBalance(
            asset = assetInfo.asset,
            feeAsset = feeAssetInfo,
            fee = fee,
            balance = balance,
            amount = finalParams.finalAmount
        )
        val (toAssetInfo, toAmount) = if (params is ConfirmParams.SwapParams) {
            Pair(
                assetRepository.getById(wallet.accounts, params.toAssetId).getOrNull()
                    ?.firstOrNull(),
                params.toAmount
            )
        } else {
            Pair(null, null)
        }
        setState {
            copy(
                screen = ConfirmStateScreen.Loaded,
                walletName = session.wallet.name,
                currency = session.currency,
                assetInfo = assetInfo,
                feeAssetInfo = feeAssetInfo,
                toAssetInfo = toAssetInfo,
                toAmount = toAmount,
                signerParams = finalParams,
                error = error,
                cells = listOf(
                    from(),
                    recipient(),
                    memo(),
                    network(),
                    fee(),
                ).mapNotNull { it },
            )
        }
    }


    private fun getBalance(assetInfo: AssetInfo, params: ConfirmParams): BigInteger {
        return when (params) {
            is ConfirmParams.TransferParams,
            is ConfirmParams.SwapParams,
            is ConfirmParams.TokenApprovalParams -> assetInfo.balances.available().atomicValue
        }
    }

    private fun validateBalance(
        asset: Asset,
        feeAsset: AssetInfo,
        fee: Fee,
        balance: BigInteger,
        amount: BigInteger,
    ): ConfirmError {
        if (feeAsset.balances.available().atomicValue < fee.amount) {
            return ConfirmError.InsufficientFee(
                "${feeAsset.asset.id.chain.asset().name}(${feeAsset.asset.symbol})"
            )
        }
        if (balance < amount) {
            return ConfirmError.InsufficientBalance(asset.symbol)
        }
        return ConfirmError.None
    }


    fun onSend() {
        setState { copy(sending = true) }
        val currentState = uiState.value
        if (currentState.assetInfo == null || currentState.signerParams == null) {
            setState { copy(screen = ConfirmStateScreen.Fatal(error = "Transaction data incorrect")) }
            return
        }
        val asset = currentState.assetInfo.asset
        val owner = currentState.assetInfo.owner
        val destinationAddress = currentState.signerParams.input.destination()
        val fee = currentState.signerParams.info.fee()
        val memo = currentState.signerParams.input.memo() ?: ""
        val type = currentState.signerParams.input.getTxType()

        val metadata = when (val input = currentState.signerParams.input) {
            is ConfirmParams.SwapParams -> {

                Json.encodeToString(
                    TransactionSwapMetadata(
                        fromAsset = input.fromAssetId,
                        toAsset = input.toAssetId,
                        fromValue = input.fromAmount.toString(),
                        toValue = input.toAmount.toString(),
                    )
                )
            }
            else -> null
        }

        viewModelScope.launch {
            val session = sessionRepository.getSession()
            if (session == null) {
                setState { copy(screen = ConfirmStateScreen.Fatal(error = "Wallet not available")) }
                return@launch
            }
            val broadcastResult = withContext(Dispatchers.IO) {
                val password = passwordStore.getPassword(session.wallet.id)
                val privateKey = loadPrivateKeyOperator(session.wallet.id, asset.id.chain, password) ?:
                return@withContext Result.failure(Exception("privateKey is null"))

                val signResult = signTransfer(currentState.signerParams, privateKey)
                val sign = signResult.getOrNull()
                    ?: return@withContext Result.failure(signResult.exceptionOrNull() ?: Exception("Sign error"))

                broadcastProxy.broadcast(owner, sign, type)
            }

            broadcastResult.onSuccess { txHash ->
                transactionRepository.addTransaction(
                    hash = txHash,
                    assetId = asset.id,
                    owner = owner,
                    to = destinationAddress,
                    state = TransactionState.Pending,
                    fee = fee,
                    amount = currentState.signerParams.finalAmount,
                    memo = memo,
                    type = type,
                    metadata = metadata,
                    direction = if (destinationAddress == owner.address) TransactionDirection.SelfTransfer else TransactionDirection.Outgoing,
                )
                setState { copy(txHash = txHash) }
            }.onFailure { err ->
                setState { copy(sending = false, screen = ConfirmStateScreen.Fatal(error = err.message ?: "Can't send asset")) }
            }
        }
    }

    private fun from(): CellEntity {
        return CellEntity(label = Res.string.transfer_from, data = "${uiState.value.walletName ?: ""} (${uiState.value.assetInfo?.owner?.address?.getAddressEllipsisText()})")
    }

    private fun recipient(): CellEntity? {
        val input = uiState.value.signerParams?.input ?: return null
        return when (input) {
            is ConfirmParams.SwapParams -> CellEntity(label = Res.string.swap_provider, data = input.provider)
            is ConfirmParams.TokenApprovalParams -> CellEntity(label = Res.string.swap_provider, data = input.provider)
            is ConfirmParams.TransferParams -> {
                return when {
                    input.domainName.isNullOrEmpty() -> CellEntity(label = Res.string.transaction_recipient, data = input.to)
                    else -> CellEntity(label = Res.string.transaction_recipient, support = input.to, data = input.domainName!!)
                }
            }
        }
    }


    private fun memo(): CellEntity? {
        return if (uiState.value.signerParams?.input is ConfirmParams.TransferParams) {
            val memo = uiState.value.signerParams!!.input.memo()
            if (memo.isNullOrEmpty()) {
                return null
            }
            CellEntity(label = Res.string.transfer_memo, data = memo)
        } else {
            null
        }
    }

    private fun network(): CellEntity? {
        val owner = uiState.value.assetInfo?.owner ?: return null
        return CellEntity(
            label = Res.string.transfer_network,
            data = owner.chain.asset().name,
            trailingIcon = owner.chain.getIconUrl(),
        )
    }

    private fun fee(): CellEntity? {
        val feeAmount = Crypto(uiState.value.signerParams?.info?.fee()?.amount ?: return null)
        val asset = uiState.value.feeAssetInfo?.asset ?: return null
        val feeDecimals = asset.decimals
        val feeCrypto = feeAmount.format(feeDecimals, asset.symbol, 6)
        val feeFiat = uiState.value.feeAssetInfo?.price?.let {
            feeAmount.convert(feeDecimals, it.price.price)
                .format(0, uiState.value.currency.string, 2, dynamicPlace = true)
        } ?: ""
        return CellEntity(
            label = Res.string.transfer_network_fee,
            data = feeCrypto,
            support = feeFiat,
            dropDownActions = null,
        )
    }

}