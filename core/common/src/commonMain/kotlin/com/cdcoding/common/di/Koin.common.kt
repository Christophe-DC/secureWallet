package com.cdcoding.common.di

import org.koin.core.module.Module
import org.koin.dsl.module


expect fun platformModule(): Module

val commonModule = module {
    includes(platformModule())
}
