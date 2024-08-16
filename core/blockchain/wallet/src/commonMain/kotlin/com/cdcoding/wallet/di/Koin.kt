package com.cdcoding.wallet.di

import com.cdcoding.wallet.client.DefaultWalletClient
import com.cdcoding.wallet.validator.DefaultPhraseValidator
import com.cdcoding.wallet.validator.PhraseValidator
import com.cdcoding.wallet.client.WalletClient
import com.cdcoding.wallet.operator.LoadPrivateKeyOperator
import com.cdcoding.wallet.operator.SWLoadPrivateKeyOperator
import com.cdcoding.wallet.operator.SWStorePhraseOperator
import com.cdcoding.wallet.operator.StorePhraseOperator
import com.cdcoding.wallet.validator.DefaultValidateAddressOperator
import com.cdcoding.wallet.validator.ValidateAddressOperator
import org.koin.core.module.Module
import org.koin.dsl.module


val walletModule = module {
    factory<WalletClient> { DefaultWalletClient() }
    factory<PhraseValidator> { DefaultPhraseValidator() }
    factory<ValidateAddressOperator> { DefaultValidateAddressOperator() }
    factory<LoadPrivateKeyOperator> { SWLoadPrivateKeyOperator(get()) }
    factory<StorePhraseOperator> { SWStorePhraseOperator(get()) }
}
