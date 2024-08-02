package com.cdcoding.common.utils

import com.cdcoding.model.Account
import com.cdcoding.model.Chain
import com.cdcoding.model.Wallet


fun Wallet.getAccount(chain: Chain): Account? {
    return accounts.firstOrNull { it.chain == chain }
}