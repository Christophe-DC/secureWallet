package com.cdcoding.wallet.di

import com.cdcoding.wallet.client.DefaultWalletClient
import com.cdcoding.wallet.validator.DefaultPhraseValidator
import com.cdcoding.wallet.validator.PhraseValidator
import com.cdcoding.wallet.client.WalletClient
import org.koin.core.module.Module
import org.koin.dsl.module


val walletModule = module {
    factory<WalletClient> { DefaultWalletClient() }
    factory<PhraseValidator> { DefaultPhraseValidator() }
}
