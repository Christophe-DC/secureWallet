package com.cdcoding.network.client.bitcoin

import com.cdcoding.model.Account
import com.cdcoding.model.AssetId
import com.cdcoding.model.Chain
import com.cdcoding.model.Crypto
import com.cdcoding.model.Fee
import com.cdcoding.model.GasFee
import com.cdcoding.model.bitcoin.BitcoinUTXO
import com.cdcoding.network.util.fold
import com.cdcoding.wallet.operator.ChainTypeProxy
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.DecimalMode
import com.ionspin.kotlin.bignum.decimal.RoundingMode
import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.BigInteger.Companion.max
import com.trustwallet.core.AnySigner
import com.trustwallet.core.BitcoinSigHashType
import com.trustwallet.core.CoinTypeConfiguration
import com.trustwallet.core.bitcoin.SigningInput
import com.trustwallet.core.bitcoin.TransactionPlan
import com.trustwallet.core.common.SigningError
import com.trustwallet.core.plan


class BitcoinFee {
    suspend operator fun invoke(
        apiClient: BitcoinApiClient,
        utxos: List<BitcoinUTXO>,
        account: Account,
        recipient: String,
        amount: BigInteger,
    ): Fee {
        val ownerAddress = account.address
        val chain = account.chain
        val price = estimateFeePrice(chain, apiClient)
        val limit = calcFee(chain, ownerAddress, recipient, amount.longValue(), price.longValue(), utxos)

        return GasFee(
            feeAssetId = AssetId(chain),
            maxGasPrice = price,
            limit = limit
        )
    }

    private suspend fun estimateFeePrice(chain: Chain, apiClient: BitcoinApiClient): BigInteger {
        val decimals = CoinTypeConfiguration.getDecimals(ChainTypeProxy().invoke(chain))
        val minimumByteFee = getMinimumByteFee(chain)
        return apiClient.estimateFee(getFeePriority(chain)).fold(
            {
                val networkFeePerKb = Crypto(it.result, decimals).atomicValue
                val feePerByte = BigDecimal.fromBigInteger(networkFeePerKb).divide(
                        BigDecimal.fromInt(1000),
                        DecimalMode(roundingMode = RoundingMode.CEILING, decimalPrecision = 3)
                    ).toBigInteger()
                return max(minimumByteFee, feePerByte)
            }
        ) {
            minimumByteFee
        }
    }

    private fun calcFee(
        chain: Chain,
        from: String,
        to: String,
        amount: Long,
        bytePrice: Long,
        utxos: List<BitcoinUTXO>,
    ): BigInteger {
        val coinType = ChainTypeProxy().invoke(chain)
        if (utxos.isEmpty()) {
            return BigInteger.ZERO
        }
        val total = utxos.map { it.value.toLong() }.reduce { x, y -> x + y }
        if (total == 0L) {
            return BigInteger.ZERO // empty balance
        }
        val input = SigningInput(
            hash_type = BitcoinSigHashType.All.value.toInt(),
            byte_fee = bytePrice,
            amount = amount,
            use_max_amount = total == amount,
            coin_type = coinType.value.toInt(),
            to_address = to,
            change_address = from,
            utxo = utxos.getUtxoTransactions(from, coinType)
        )

        val plan = AnySigner.plan(input, coinType, TransactionPlan.ADAPTER)
        if (plan.error == SigningError.Error_not_enough_utxos || plan.error == SigningError.Error_missing_input_utxos) {
            throw IllegalStateException("Dust Error: $bytePrice")
        } else if (plan.error != SigningError.OK) {
            throw IllegalStateException(plan.error.name)
        }

        val selectedUtxos: MutableList<BitcoinUTXO> = mutableListOf()
        for (raw in plan.utxos) {
            input.utxo.indexOfFirst { it == raw }.let {
                selectedUtxos.add(utxos[it])
            }
        }

        if (utxos.isNotEmpty() && selectedUtxos.isEmpty() && amount != total && amount <= total) {
            return calcFee(chain, from, to, total, bytePrice, utxos)
        }
        return BigInteger(plan.fee / bytePrice)
    }

    private fun getMinimumByteFee(chain: Chain) = when (chain) {
        Chain.Litecoin -> BigInteger(5)
        Chain.Doge -> BigInteger(1000)
        else -> BigInteger.ONE
    }

    private fun getFeePriority(chain: Chain) = when (chain) {
        Chain.Litecoin,
        Chain.Doge -> "1"
        else -> "3"
    }
}