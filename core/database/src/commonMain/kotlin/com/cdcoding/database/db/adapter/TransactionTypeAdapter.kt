package com.cdcoding.database.db.adapter

import app.cash.sqldelight.ColumnAdapter
import com.cdcoding.database.db.model.TransactionTypeEntity

class TransactionTypeAdapter: ColumnAdapter<TransactionTypeEntity, String> {

    private val currentValue by nonSynchronizedLazy {
        TransactionTypeEntity.entries.associateBy { it.string }
    }

    override fun decode(databaseValue: String): TransactionTypeEntity {
        return currentValue.getValue(databaseValue)
    }

    override fun encode(value: TransactionTypeEntity): String {
        return value.string
    }
}
