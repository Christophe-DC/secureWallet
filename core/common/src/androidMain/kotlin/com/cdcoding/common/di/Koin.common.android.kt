package com.cdcoding.common.di

import com.cdcoding.common.utils.AndroidDataDir
import com.cdcoding.common.utils.DataDir
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module {
    return module {
        single<DataDir> { AndroidDataDir(get()) }
    }
}