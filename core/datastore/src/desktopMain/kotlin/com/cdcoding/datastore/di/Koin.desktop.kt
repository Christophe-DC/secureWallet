package com.cdcoding.datastore.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.cdcoding.datastore.createDataStore
import com.cdcoding.datastore.dataStoreFileName
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module {
    return module {
        single<DataStore<Preferences>> { createDataStore{ dataStoreFileName } }
    }
}