package com.cdcoding.common.di

import com.cdcoding.common.utils.DataDir
import com.cdcoding.common.utils.DesktopDataDir
import org.koin.dsl.module
import org.koin.core.module.Module


actual fun platformModule(): Module {
    return module {
        single<DataDir> { DesktopDataDir() }
    }
}