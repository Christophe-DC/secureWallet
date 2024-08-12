package com.cdcoding.amount.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.cdcoding.common.utils.CommonViewModel
import com.cdcoding.data.repository.AssetRepository
import com.cdcoding.data.repository.SessionRepository
import com.cdcoding.model.Asset
import com.cdcoding.model.AssetId
import com.cdcoding.model.AssetInfo
import com.cdcoding.model.ConfirmParams
import com.cdcoding.model.Crypto
import com.cdcoding.model.InputCurrency
import com.cdcoding.model.TransactionType
import com.cdcoding.model.numberParse
import com.ionspin.kotlin.bignum.integer.BigInteger
import kotlinx.coroutines.launch

class AmountViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val sessionRepository: SessionRepository,
    private val assetRepository: AssetRepository
) : CommonViewModel<AmountUIState, AmountEvent, AmountIntent>() {

    private lateinit var destinationAddress: String
    private lateinit var addressDomain: String
    private lateinit var memo: String

    override fun createInitialState(): AmountUIState {
        return AmountUIState()
    }

    override fun handleIntent(intent: AmountIntent) {
        when (intent) {
            is AmountIntent.OnNext -> onNext(intent.onNextComplete)
            is AmountIntent.OnUpdateAmount -> updateAmount(intent.input, intent.maxAmount)
            AmountIntent.OnMaxAmount -> onMaxAmount()
        }
    }

    init {
        viewModelScope.launch {
            initialize()
        }
    }

    private suspend fun initialize() {

        val assetId: AssetId? = savedStateHandle["assetId"]
        if (assetId == null) {
            setState { copy(screen = AmountStateScreen.Fatal(error = "Missing assetId")) }
            return
        }
        destinationAddress = savedStateHandle["destinationAddress"] ?: ""
        addressDomain = savedStateHandle["addressDomain"] ?: ""
        memo = savedStateHandle["memo"] ?: ""
        val txType: TransactionType = savedStateHandle["txType"] ?: TransactionType.Transfer

        val session = sessionRepository.getSession() ?: return
        val wallet = session.wallet
        val assetInfo = assetRepository.getById(wallet.accounts, assetId).getOrNull()?.firstOrNull()

        if (assetInfo == null) {
            setState { copy(screen = AmountStateScreen.Fatal(error = "Stake error")) }
            return
        }
        setState {
            copy(
                loading = false,
                screen = AmountStateScreen.Loaded,
                assetInfo = assetInfo,
                txType = txType,
                availableAmount = assetInfo.balances.available()
                    .format(assetInfo.asset.decimals, assetInfo.asset.symbol, 8),
                equivalent = " "
            )
        }

    }


    private fun onNext(onConfirm: (ConfirmParams) -> Unit) {
        val assetInfo = uiState.value.assetInfo
        val inputCurrency = InputCurrency.InCrypto
        val maxAmount = uiState.value.maxAmount

        if (assetInfo == null) {
            setState { copy(screen = AmountStateScreen.Fatal(error = "AssetInfo error")) }
            return
        }
        val asset = assetInfo.asset
        val decimals = asset.decimals
        val price = assetInfo.price?.price?.price ?: 0.0

        val minimumValue = BigInteger.ZERO
        val inputError =
            validateAmount(asset, uiState.value.amount, inputCurrency, price, minimumValue)
        if (inputError != AmountError.None) {
            setState { copy(error = inputError) }
            return
        }

        val amount = inputCurrency.getAmount(uiState.value.amount, decimals, price)

        val balanceError = validateBalance(
            assetInfo = assetInfo,
            txType = uiState.value.txType,
            amount = amount
        )
        if (balanceError != AmountError.None) {
            setState { copy(error = balanceError) }
            return
        }
        setState {
            copy(
                loading = false,
                error = AmountError.None
            )
        }
        val params = when (uiState.value.txType) {
            TransactionType.Transfer -> ConfirmParams.TransferParams(
                assetId = asset.id,
                amount = amount.atomicValue,
                domainName = addressDomain,
                to = destinationAddress,
                isMaxAmount = maxAmount,
                memo = memo,
            )

            TransactionType.Swap,
            TransactionType.TokenApproval -> throw IllegalArgumentException()
        }
        onConfirm(params)
    }


    private fun updateAmount(input: String, maxAmount: Boolean = false) {
        setState {
            copy(
                equivalent = calcEquivalent(input, InputCurrency.InCrypto),
                error = AmountError.None,
                maxAmount = maxAmount,
                amount = input
            )
        }
    }


    private fun onMaxAmount() {
        val asset = uiState.value.assetInfo ?: return
        val balance = uiState.value.assetInfo?.balances?.available() ?: Crypto(BigInteger.ZERO)
        updateAmount(balance.value(asset.asset.decimals).toStringExpanded(), true)
    }

    private fun calcEquivalent(inputAmount: String, inputCurrency: InputCurrency): String {
        val currency = sessionRepository.getSession()?.currency ?: return ""
        val assetInfo = uiState.value.assetInfo ?: return ""
        val price = assetInfo.price?.price?.price ?: return ""
        val decimals = assetInfo.asset.decimals

        val amount = if (validateAmount(
                assetInfo.asset,
                inputAmount,
                inputCurrency,
                price,
                BigInteger.ZERO
            ) == AmountError.None
        ) {
            inputAmount
        } else {
            "0"
        }

        return if (inputCurrency == InputCurrency.InFiat) {
            val unit = inputCurrency.getAmount(amount, decimals = decimals, price)
            unit.format(0, currency.string, decimalPlace = 2, dynamicPlace = true, zeroFraction = 0)
        } else {
            val unit = Crypto(amount.numberParse(), decimals).convert(decimals, price)
            unit.format(0, currency.string, decimalPlace = 2, dynamicPlace = true, zeroFraction = 0)
        }
    }

    private fun validateAmount(
        asset: Asset,
        amount: String,
        inputCurrency: InputCurrency,
        price: Double,
        minValue: BigInteger
    ): AmountError {
        if (amount.isEmpty()) {
            return AmountError.Required
        }
        try {
            amount.numberParse()
        } catch (err: Throwable) {
            return AmountError.IncorrectAmount
        }
        if (inputCurrency == InputCurrency.InFiat && price <= 0.0) {
            return AmountError.Unavailable
        }
        val crypto = Crypto(amount.numberParse(), asset.decimals)
        if (BigInteger.ZERO != minValue && crypto.atomicValue < minValue) {
            return AmountError.MinimumValue(
                Crypto(minValue).format(
                    asset.decimals,
                    asset.symbol,
                    decimalPlace = 2
                )
            )
        }
        return AmountError.None
    }

    private fun validateBalance(
        assetInfo: AssetInfo,
        txType: TransactionType,
        amount: Crypto
    ): AmountError {
        if (amount.atomicValue == BigInteger.ZERO) {
            return AmountError.ZeroAmount
        }
        val availableAmount = when (txType) {
            TransactionType.Transfer,
            TransactionType.Swap,
            TransactionType.TokenApproval -> assetInfo.balances.available()
        }
        if (amount.atomicValue > availableAmount.atomicValue) {
            return AmountError.InsufficientBalance(assetInfo.asset.name)
        }
        return AmountError.None
    }


}