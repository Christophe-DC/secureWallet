package com.cdcoding.wallet.di


import com.cdcoding.wallet.client.AndroidStorePhraseClient
import com.cdcoding.wallet.client.StorePhraseClient
import com.cdcoding.wallet.validator.AndroidPhraseValidator
import com.cdcoding.wallet.validator.PhraseValidator
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module {
    return module {
        single<PhraseValidator> { AndroidPhraseValidator() }
        single<StorePhraseClient> { AndroidStorePhraseClient(get()) }
    }
}