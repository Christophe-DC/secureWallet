package com.cdcoding.data.local.db.adapter

import app.cash.sqldelight.ColumnAdapter
import com.cdcoding.data.local.db.model.CurrencyEntity
import com.cdcoding.data.local.db.model.WalletTypeEntity

class CurrencyAdapter: ColumnAdapter<CurrencyEntity, String> {

    private val currentValue by nonSynchronizedLazy {
        CurrencyEntity.entries.associateBy { it.string }
    }

    override fun decode(databaseValue: String): CurrencyEntity {
        return currentValue.getValue(databaseValue)
    }

    override fun encode(value: CurrencyEntity): String {
        return value.string
    }
}
