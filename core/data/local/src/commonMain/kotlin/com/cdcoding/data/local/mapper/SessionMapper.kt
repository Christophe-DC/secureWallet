package com.cdcoding.data.local.mapper


import com.cdcoding.data.local.db.model.asEntity
import com.cdcoding.data.local.db.model.asExternal
import com.cdcoding.local.db.SessionEntity
import com.cdcoding.model.Session
import com.cdcoding.model.Wallet

fun SessionEntity.asExternal(wallet: Wallet): Session {
    return Session(
        wallet = wallet,
        currency = this.currency.asExternal(),
    )
}


fun Session.asEntity(): SessionEntity {
    return SessionEntity(
        id = 1,
        walletId = this.wallet.id,
        currency = this.currency.asEntity(),
    )
}
