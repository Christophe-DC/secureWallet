package com.cdcoding.datastore.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.koin.core.module.Module
import org.koin.dsl.module
import com.cdcoding.datastore.createDataStoreIos

actual fun platformModule(): Module {
    return module {
        single<DataStore<Preferences>> { createDataStoreIos() }
    }
}