package com.cdcoding.database.db.adapter

import app.cash.sqldelight.ColumnAdapter
import com.cdcoding.database.db.model.BalanceTypeEntity

class BalanceTypeAdapter: ColumnAdapter<BalanceTypeEntity, String> {

    private val currentValue by nonSynchronizedLazy {
        BalanceTypeEntity.entries.associateBy { it.string }
    }

    override fun decode(databaseValue: String): BalanceTypeEntity {
        return currentValue.getValue(databaseValue)
    }

    override fun encode(value: BalanceTypeEntity): String {
        return value.string
    }
}
