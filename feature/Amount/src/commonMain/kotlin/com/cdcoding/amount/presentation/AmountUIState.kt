package com.cdcoding.amount.presentation

import com.cdcoding.model.AssetInfo
import com.cdcoding.model.DelegationValidator
import com.cdcoding.model.InputCurrency
import com.cdcoding.model.TransactionType


data class AmountUIState(
    val loading: Boolean = false,
    val error: AmountError = AmountError.None,
    val txType: TransactionType = TransactionType.Transfer,
    val assetInfo: AssetInfo? = null,
    val availableAmount: String = "0",
    val equivalent: String = "",
    val maxAmount: Boolean = false,
    val amount: String = "",
    val inputCurrency: InputCurrency = InputCurrency.InCrypto,
    val screen: AmountStateScreen = AmountStateScreen.Loading

)

sealed interface AmountStateScreen {
    data object Loading : AmountStateScreen
    data object Loaded : AmountStateScreen
    data class Fatal(val error: String) : AmountStateScreen
}

sealed interface AmountError {
    data object None : AmountError
    data object Init : AmountError
    data object Required : AmountError
    data object Unavailable : AmountError
    data object IncorrectAmount : AmountError
    data object ZeroAmount : AmountError
    class InsufficientBalance(val assetName: String) : AmountError
    class MinimumValue(val minimumValue: String) : AmountError
}