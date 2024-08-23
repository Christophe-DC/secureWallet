package com.cdcoding.data.repository

import kotlinx.coroutines.flow.Flow
import com.cdcoding.database.db.SessionDao
import com.cdcoding.model.Session
import com.cdcoding.model.Wallet

interface SessionRepository {
    fun session(): Flow<Session?>
    fun getSession(): Session?
    fun hasSession(): Boolean
    suspend fun setSession(walletId: String)
    suspend fun reset()
}


class DefaultSessionRepository(
    private val sessionDao: SessionDao,
) : SessionRepository {

    override fun session(): Flow<Session?> = sessionDao.session()
    override fun getSession(): Session? = sessionDao.getSession()
    override fun hasSession(): Boolean = sessionDao.hasSession()
    override suspend fun setSession(walletId: String) = sessionDao.setWallet(walletId)
    override suspend fun reset() = sessionDao.reset()

}