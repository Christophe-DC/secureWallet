package com.cdcoding.confirm.presentation

import com.cdcoding.core.designsystem.table.CellEntity
import com.cdcoding.model.AssetInfo
import com.cdcoding.model.ConfirmParams
import com.cdcoding.model.Currency
import com.cdcoding.model.DelegationValidator
import com.cdcoding.model.InputCurrency
import com.cdcoding.model.SignerParams
import com.cdcoding.model.TransactionType
import com.ionspin.kotlin.bignum.integer.BigInteger
import org.jetbrains.compose.resources.StringResource


data class ConfirmUIState(
    val loading: Boolean = false,
    val txType: TransactionType = TransactionType.Transfer,
    val fatalError: ConfirmError = ConfirmError.None,
    val currency: Currency = Currency.USD,
    val walletName: String? = null,
    val assetInfo: AssetInfo? = null,
    val feeAssetInfo: AssetInfo? = null,
    val toAssetInfo: AssetInfo? = null,
    val toAmount: BigInteger? = null,
    val signerParams: SignerParams? = null,
    val error: ConfirmError = ConfirmError.None,
    val cells: List<CellEntity> = emptyList(),
    val sending: Boolean = false,
    val txHash: String = "",
    val screen: ConfirmStateScreen = ConfirmStateScreen.Loading

)

sealed interface ConfirmStateScreen {
    data object Loading : ConfirmStateScreen
    data object Loaded : ConfirmStateScreen
    data class Fatal(val error: String) : ConfirmStateScreen
}