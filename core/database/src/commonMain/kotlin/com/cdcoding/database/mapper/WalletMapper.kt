package com.cdcoding.database.mapper


import com.cdcoding.database.db.model.asEntity
import com.cdcoding.database.db.model.asExternal
import com.cdcoding.local.db.WalletEntity
import com.cdcoding.model.Account
import com.cdcoding.model.Wallet

fun WalletEntity.asExternal(accounts: List<Account>): Wallet {
    return Wallet(
        id = this.id,
        name = this.name,
        index = this.walletIndex,
        type = this.type.asExternal(),
        accounts = accounts,
    )
}


fun Wallet.asEntity(): WalletEntity {
    return WalletEntity(
        id = this.id,
        name = this.name,
        walletIndex = this.index,
        type = this.type.asEntity()
    )
}
