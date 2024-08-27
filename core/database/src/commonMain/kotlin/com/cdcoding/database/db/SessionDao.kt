package com.cdcoding.database.db

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.cdcoding.database.db.model.asEntity
import com.cdcoding.database.mapper.asExternal
import com.cdcoding.local.db.SecureWalletDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import com.cdcoding.model.Currency
import com.cdcoding.model.Session
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

interface SessionDao {
    fun session(): Flow<Session?>
    fun getSession(): Session?
    fun hasSession(): Boolean
    suspend fun setWallet(walletId: String)
    suspend fun reset()
    suspend fun setCurrency(currency: Currency)
}

class DefaultSessionDao(
    database: SecureWalletDatabase,
    private val walletDao: WalletDao
) : SessionDao {

    private val sessionQueries = database.sessionQueries

    override fun session(): Flow<Session?> {
        return sessionQueries.getSession()
            .asFlow()
            .mapToOneOrNull(Dispatchers.IO)
            .map { sessionEntity ->
                if (sessionEntity == null) return@map null
                val wallet = walletDao.getWallet(sessionEntity.walletId)
                sessionEntity.asExternal(wallet)
            }
    }

    override fun getSession(): Session? {
        val sessionEntity = sessionQueries.getSession().executeAsOneOrNull() ?: return null
        val wallet = walletDao.getWallet(sessionEntity.walletId)
        return sessionEntity.asExternal(wallet)
    }

    override fun hasSession(): Boolean = getSession() != null

    override suspend fun setWallet(walletId: String)  = withContext(Dispatchers.IO) {
        sessionQueries.transaction {
            sessionQueries.updateSession(
                walletId = walletId,
                currency = Currency.USD.asEntity(),
            )
        }
    }

    override suspend fun reset()  = withContext(Dispatchers.IO) {
        sessionQueries.transaction {
            sessionQueries.reset()
        }
    }

    override suspend fun setCurrency(currency: Currency) = withContext(Dispatchers.IO) {
        val session = getSession() ?: return@withContext
        sessionQueries.transaction {
            sessionQueries.updateSession(
                walletId = session.wallet.id,
                currency = currency.asEntity(),
            )
        }
    }
}
