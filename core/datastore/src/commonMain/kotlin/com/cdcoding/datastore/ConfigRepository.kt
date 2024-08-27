package com.cdcoding.datastore


interface ConfigRepository {

    suspend fun updateDeviceId()

    suspend fun getDeviceId(): String
    suspend fun increaseSubscriptionVersion()
    suspend fun getTxSyncTime(): Long
    suspend fun setTxSyncTime(time: Long)
}