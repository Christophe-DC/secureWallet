package com.cdcoding.wallet.validator

import com.cdcoding.model.Chain
import com.cdcoding.wallet.operator.ChainTypeProxy
import com.trustwallet.core.AnyAddress


class DefaultValidateAddressOperator constructor() : ValidateAddressOperator {
    override operator fun invoke(address: String, chain: Chain): Result<Boolean>
       = Result.success(AnyAddress.isValid(address, ChainTypeProxy().invoke(chain)))
}