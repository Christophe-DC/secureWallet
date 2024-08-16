package com.cdcoding.confirm.presentation

import androidx.compose.runtime.Composable
import com.cdcoding.core.resource.Res
import org.jetbrains.compose.resources.stringResource
import com.cdcoding.core.resource.transfer_confirm
import com.cdcoding.core.resource.transfer_insufficient_balance
import com.cdcoding.core.resource.transfer_insufficient_network_fee_balance
import com.cdcoding.core.resource.errors_transfer
import com.cdcoding.core.resource.confirm_fee_error

sealed class ConfirmError(val message: String) {

    data object None : ConfirmError("")

    class Init(message: String) : ConfirmError(message)

    data object CalculateFee : ConfirmError("Calculate fee error")

    data object TransactionIncorrect : ConfirmError("Transaction data incorrect")

    data object WalletNotAvailable : ConfirmError("Wallet not available")

    class InsufficientBalance(val chainTitle: String) : ConfirmError("Insufficient Balance")

    class InsufficientFee(val chainTitle: String) : ConfirmError("Insufficient Fee")

    class SignFail(message: String) : ConfirmError(message)

    class BroadcastError(message: String) : ConfirmError(message)

    @Composable
    fun toStringResource(): String {
        return when (this) {
            None -> stringResource(Res.string.transfer_confirm)
            is InsufficientBalance -> stringResource(
                Res.string.transfer_insufficient_balance,
                chainTitle,
            )
            is InsufficientFee -> stringResource(
                Res.string.transfer_insufficient_network_fee_balance,
                chainTitle,
            )
            is Init,
            is BroadcastError,
            is SignFail,
            TransactionIncorrect,
            WalletNotAvailable -> stringResource(
                Res.string.errors_transfer,
                message
            )
            CalculateFee -> stringResource(Res.string.confirm_fee_error)
        }
    }
}