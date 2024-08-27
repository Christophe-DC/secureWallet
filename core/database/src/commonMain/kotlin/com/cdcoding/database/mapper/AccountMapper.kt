package com.cdcoding.database.mapper


import com.cdcoding.database.db.model.asEntity
import com.cdcoding.database.db.model.asExternal
import com.cdcoding.local.db.AccountEntity
import com.cdcoding.model.Account

fun AccountEntity.asExternal(): Account {
    return Account(
        id = this.id,
        chain = this.chain.asExternal(),
        address = this.address,
        derivationPath = this.derivationPath,
        extendedPublicKey = this.extendedPublicKey
    )
}


fun Account.asEntity(walletId: String): AccountEntity {
    return AccountEntity(
        id = this.id,
        chain = this.chain.asEntity(),
        address = this.address,
        derivationPath = this.derivationPath,
        extendedPublicKey = this.extendedPublicKey,
        walletId = walletId
    )
}
