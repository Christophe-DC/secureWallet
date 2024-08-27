package com.cdcoding.model

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.BigDecimal.Companion.parseStringWithMode
import com.ionspin.kotlin.bignum.decimal.DecimalMode
import com.ionspin.kotlin.bignum.decimal.RoundingMode
import kotlin.math.min
import kotlin.math.abs
import kotlin.math.pow

abstract class CountingUnit<T, C>(
    val atomicValue: T,
) {
    abstract fun convert(decimals: Int, price: Double): C

    abstract fun value(decimals: Int): BigDecimal

    abstract fun format(
        decimals: Int,
        symbol: String,
        decimalPlace: Int,
        showSign: SignMode = SignMode.NoPLus,
        dynamicPlace: Boolean = false,
        zeroFraction: Int = 0,
    ): String

    fun cutFraction(value: BigDecimal, decimalPlace: Int, dynamicDecimal: Boolean = false): Pair<BigDecimal, Int> {
        if (value.isZero()) {
            return Pair(value, decimalPlace)
        }

        val whole = BigDecimal.fromBigInteger(value.toBigInteger().abs())
        val fraction = value.abs().minus(whole).toPlainString()//.stripTrailingZeros().toPlainString()

        val result = if (decimalPlace == -1) {
            value
        } else {
            val minDecimalPlaces = min(decimalPlace, fraction.length - 2)
            val result = if (minDecimalPlaces > 0) {
               // BigDecimal.("${whole}.${fraction.substring(2 until minDecimalPlaces + 2)}")
                BigDecimal.parseString("${whole.toBigInteger()}.${fraction.substring(2 until minDecimalPlaces + 2)}")
                /*
                parseStringWithMode(
                    "${whole}.${fraction.substring(2 until minDecimalPlaces + 2)}"
                    .dropLastWhile { it == '0' }, value.decimalMode).roundSignificand(
                    value.decimalMode
                )*/
            } else {
                whole
            }
            if (value > 0) {
                result
            } else {
                -result
            }
        }

        return if (result <= 0 && dynamicDecimal && decimalPlace < fraction.toString().length) {
            cutFraction(value, decimalPlace + 2, true)
        } else {
            Pair(result, decimalPlace)
        }
    }

    enum class SignMode {
        NoSign,
        NoPLus,
        All,
    }
}