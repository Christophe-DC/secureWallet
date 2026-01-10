package com.cdcoding.datasource.di


import org.koin.core.module.Module
import org.koin.dsl.module

expect fun platformModule(): Module

val datasourceModule = module {
    includes(platformModule())
}
