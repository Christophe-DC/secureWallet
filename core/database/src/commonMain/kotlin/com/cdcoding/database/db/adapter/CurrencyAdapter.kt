package com.cdcoding.database.db.adapter

import app.cash.sqldelight.ColumnAdapter
import com.cdcoding.database.db.model.CurrencyEntity

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
