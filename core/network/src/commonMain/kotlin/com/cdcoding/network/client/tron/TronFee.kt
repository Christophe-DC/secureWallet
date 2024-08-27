package com.cdcoding.network.client.tron

import com.cdcoding.model.Account
import com.cdcoding.model.AssetId
import com.cdcoding.model.AssetSubtype
import com.cdcoding.model.Fee
import com.cdcoding.model.tron.TronAccountRequest
import com.cdcoding.network.util.fold
import com.cdcoding.network.util.getOrNull
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.integer.BigInteger
import com.trustwallet.core.Base58
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class TronFee {
    @OptIn(ExperimentalStdlibApi::class)
    suspend operator fun invoke(
        apiClient: TronApiClient,
        account: Account,
        contractAddress: String?,
        recipientAddress: String,
        value: BigInteger,
        type: AssetSubtype,
    ): Fee = withContext(Dispatchers.IO) {
        val isNewAccountJob = async {
            apiClient.getAccount(
                TronAccountRequest(
                    address = Base58.decode(account.address)?.toHexString() ?: "",
                    visible = false
                )
            ).fold({ it.address.isNullOrEmpty() }) { true }
        }
        val accountUsageJob = async {
            apiClient.getAccountUsage(
                TronAccountRequest(
                    address = Base58.decode(account.address)?.toHexString() ?: "",
                    visible = false
                )
            ).getOrNull()
        }

        val paramsJob = async { apiClient.getChainParameters().fold({ it.chainParameter }) {null} }
        val isNewAccount = isNewAccountJob.await()
        val params = paramsJob.await()
        val accountUsage = accountUsageJob.await()

        val newAccountFeeInSmartContract = params?.firstOrNull { it.key == "getCreateNewAccountFeeInSystemContract" }?.value
        val newAccountFee = params?.firstOrNull{ it.key == "getCreateAccountFee" }?.value
        val energyFee = params?.firstOrNull { it.key == "getEnergyFee" }?.value
        if (newAccountFeeInSmartContract == null || newAccountFee == null || energyFee == null) {
            throw Exception("unknown key")
        }
        val fee = when (type) {
            AssetSubtype.NATIVE -> {
                val availableBandwidth =
                    accountUsage?.freeNetLimit ?: (0 - (accountUsage?.freeNetUsed ?: 0))
                val coinTransferFee = if (availableBandwidth >= 300) BigInteger.ZERO else BigInteger(280_000)
                if (isNewAccount) coinTransferFee + BigInteger(newAccountFee) else coinTransferFee
            }
            else -> {
                // https://developers.tron.network/docs/set-feelimit#how-to-estimate-energy-consumption
                val gasLimit = estimateTRC20Transfer(
                    apiClient = apiClient,
                    ownerAddress = account.address,
                    recipientAddress =  recipientAddress,
                    contractAddress = contractAddress ?: throw IllegalArgumentException("Incorrect contract on fee calculation"),
                    value = value,
                )
                val tokenTransfer = BigInteger(energyFee) * gasLimit.add(gasLimit.multiply(
                    BigDecimal.parseString("0.2")
                )).toBigInteger()
                if (isNewAccount) tokenTransfer + BigInteger(newAccountFeeInSmartContract) else tokenTransfer
            }
        }
        Fee(
            AssetId(account.chain),
            fee
        )
    }

    // https://developers.tron.network/docs/set-feelimit#how-to-estimate-energy-consumption
    @OptIn(ExperimentalStdlibApi::class)
    private suspend fun estimateTRC20Transfer(
        apiClient: TronApiClient,
        ownerAddress: String,
        recipientAddress: String,
        contractAddress: String,
        value: BigInteger
    ): BigDecimal {
        val address = Base58.decode(recipientAddress)?.toHexString() ?: ""
        val parameter = arrayOf(
            address,
            value.toByteArray().toHexString()
        ).joinToString(separator = "") { it.padStart(64, '0') }
        val result = apiClient.triggerSmartContract(
            contractAddress = contractAddress,
            functionSelector = "transfer(address,uint256)",
            parameter = parameter,
            feeLimit = 0L,
            callValue = 0L,
            ownerAddress = ownerAddress,
            visible = true
        ).getOrNull()
        if (result == null || !result.result.message.isNullOrEmpty()) {
            throw IllegalStateException("Can't get gas limit")
        }
        return BigDecimal.fromInt(result.energy_used)
    }
}