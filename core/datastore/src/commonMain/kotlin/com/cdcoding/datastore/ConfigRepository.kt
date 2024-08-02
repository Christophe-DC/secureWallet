package com.cdcoding.datastore


interface ConfigRepository {

    suspend fun updateDeviceId()

    suspend fun getDeviceId(): String
    suspend fun increaseSubscriptionVersion()
}