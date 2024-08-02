package com.cdcoding.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.cdcoding.common.utils.uuid4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class OfflineFirstConfigRepository(
    private val dataStore: DataStore<Preferences>,
) : ConfigRepository {

    private companion object {
        const val DEVICE_ID = "deviceId"
        const val SUBSCRIPTION_VERSION = "subscriptionVersion"
    }

    private val deviceIdKey = stringPreferencesKey(DEVICE_ID)
    private val subscriptionVersion = intPreferencesKey(SUBSCRIPTION_VERSION)


    override suspend fun updateDeviceId() {
        val deviceId = dataStore.data.map { preferences -> preferences[deviceIdKey] }.first()
        if (deviceId.isNullOrEmpty()) {
            dataStore.edit { preferences -> preferences[deviceIdKey] = uuid4().substring(0, 31) }
        }
    }

    override suspend fun increaseSubscriptionVersion() {
        val currentVersion =
            dataStore.data.map { preferences -> preferences[subscriptionVersion] }.first() ?: 0
        dataStore.edit { preferences -> preferences[subscriptionVersion] = currentVersion + 1 }
    }

    override suspend fun getDeviceId(): String {
        return  dataStore.data.map { preferences -> preferences[deviceIdKey] }.firstOrNull() ?: "android_debug_device_id"
    }


}