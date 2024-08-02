package com.cdcoding.data.repository

import kotlinx.coroutines.flow.Flow
import com.cdcoding.database.db.SessionDao
import com.cdcoding.model.Session

interface SessionRepository {
    fun session(): Flow<Session?>
    fun getSession(): Session?
    fun hasSession(): Boolean
}


class DefaultSessionRepository(
    private val sessionDao: SessionDao,
) : SessionRepository {

    override fun session(): Flow<Session?> = sessionDao.session()
    override fun getSession(): Session? = sessionDao.getSession()
    override fun hasSession(): Boolean = sessionDao.hasSession()

}