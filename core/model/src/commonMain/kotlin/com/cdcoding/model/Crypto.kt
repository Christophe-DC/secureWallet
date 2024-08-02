package com.cdcoding.model

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.DecimalMode
import com.ionspin.kotlin.bignum.decimal.RoundingMode
import com.ionspin.kotlin.bignum.decimal.toBigDecimal
import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.toBigInteger


class Crypto(atomicValue: BigInteger) : CountingUnit<BigInteger, Fiat>(
    atomicValue = atomicValue,
) {
    constructor(value: String, decimals: Int) : this(value.toBigDecimal(), decimals)

    constructor(value: String) : this(value.toBigInteger())

    constructor(value: BigDecimal, decimals: Int) : this(
        value.multiply(BigDecimal.TEN.pow(decimals)).toBigInteger()
    )

    override fun convert(decimals: Int, price: Double): Fiat {
        val result = BigDecimal.fromBigInteger(atomicValue)
            .divide(BigDecimal.TEN.pow(decimals), DecimalMode(128, RoundingMode.ROUND_HALF_AWAY_FROM_ZERO, -1))
            .multiply(price.toBigDecimal())
        return Fiat(result)
    }

    override fun value(decimals: Int): BigDecimal =
        BigDecimal.fromBigInteger(atomicValue).divide(BigDecimal.TEN.pow(decimals), DecimalMode(128, RoundingMode.ROUND_HALF_AWAY_FROM_ZERO, -1))

    override fun format(
        decimals: Int,
        symbol: String,
        decimalPlace: Int,
        showSign: SignMode,
        dynamicPlace: Boolean,
        zeroFraction: Int,
    ): String {
        val (value, _) = cutFraction(value(decimals), decimalPlace, dynamicPlace)
        /*val formatter = NumberFormat.getInstance(Locale.getDefault()) as DecimalFormat
        val formatted = if (value.compareTo(BigDecimal.ZERO) == 0) {
            formatter.maximumFractionDigits = zeroFraction
            formatter.minimumFractionDigits = zeroFraction
            formatter.format(0.00)
        } else {
            formatter.maximumFractionDigits = Int.MAX_VALUE
            formatter.minimumFractionDigits = 0
            formatter.format(value)
        }*/
        val formatted = value
        val zeroCompare = value.compareTo(BigDecimal.ZERO)
        return if (zeroCompare < 0) {
            "${if (showSign != SignMode.NoSign) "-" else ""}$formatted $symbol"
        } else if (zeroCompare == 0) {
            "0.00 $symbol"
        } else {
            "${if (showSign == SignMode.All) "+" else ""}$formatted $symbol"
        }
    }
}
