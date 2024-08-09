package com.cdcoding.wallet.validator

import com.cdcoding.model.Chain


interface ValidateAddressOperator {
    operator fun invoke(address: String, chain: Chain): Result<Boolean>
}