package com.cdcoding.model

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.DecimalMode
import com.ionspin.kotlin.bignum.decimal.RoundingMode
import com.ionspin.kotlin.bignum.decimal.toBigDecimal


class Fiat(value: BigDecimal) : CountingUnit<BigDecimal, Crypto>(
    atomicValue = value,
) {
    constructor(value: Double) : this(BigDecimal.fromDouble(value))

    constructor(value: Float) : this(BigDecimal.fromFloat(value))

    override fun convert(decimals: Int, price: Double): Crypto {
        val result = atomicValue.divide(price.toBigDecimal(), DecimalMode(128, RoundingMode.ROUND_HALF_AWAY_FROM_ZERO, -1))
            .multiply(BigDecimal.TEN.pow(decimals))
            .toBigInteger()
        return Crypto(result)
    }

    override fun value(decimals: Int): BigDecimal = atomicValue

    override fun format(
        decimals: Int,
        symbol: String,
        decimalPlace: Int,
        showSign: SignMode,
        dynamicPlace: Boolean,
        zeroFraction: Int,
    ): String {
        val (value, place) = cutFraction(value(0), decimalPlace, dynamicPlace)
        val formatted = if (decimals > -1) {
            BigDecimal.fromBigDecimal(value, value.decimalMode?.copy(decimalPrecision = place.toLong())).toStringExpanded()
        } else {
            value.toStringExpanded()
        }

        val zeroCompare = value.compareTo(0.0)
        return if (zeroCompare < 0) {
            "${if (showSign != SignMode.NoSign) "-" else ""}$formatted $symbol"
        } else if (zeroCompare == 0) {
            "$formatted $symbol"
        } else {
            "${if (showSign == SignMode.All) "+" else ""}$formatted $symbol"
        }
    }
}