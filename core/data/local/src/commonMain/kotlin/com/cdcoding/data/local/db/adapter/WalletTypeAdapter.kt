package com.cdcoding.data.local.db.adapter

import app.cash.sqldelight.ColumnAdapter
import com.cdcoding.data.local.db.model.WalletTypeEntity

class WalletTypeAdapter: ColumnAdapter<WalletTypeEntity, String> {

    private val currentValue by nonSynchronizedLazy {
        WalletTypeEntity.entries.associateBy { it.string }
    }

    override fun decode(databaseValue: String): WalletTypeEntity {
        return currentValue.getValue(databaseValue)
    }

    override fun encode(value: WalletTypeEntity): String {
        return value.string
    }
}
