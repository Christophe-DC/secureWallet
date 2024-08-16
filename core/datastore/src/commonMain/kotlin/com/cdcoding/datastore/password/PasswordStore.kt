package com.cdcoding.datastore.password

interface PasswordStore {
    fun createPassword(walletId: String): String
    fun removePassword(walletId: String): Boolean
    fun getPassword(walletId: String): String
}