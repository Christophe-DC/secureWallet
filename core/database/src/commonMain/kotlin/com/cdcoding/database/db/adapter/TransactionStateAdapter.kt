package com.cdcoding.database.db.adapter

import app.cash.sqldelight.ColumnAdapter
import com.cdcoding.database.db.model.TransactionStateEntity

class TransactionStateAdapter: ColumnAdapter<TransactionStateEntity, String> {

    private val currentValue by nonSynchronizedLazy {
        TransactionStateEntity.entries.associateBy { it.string }
    }

    override fun decode(databaseValue: String): TransactionStateEntity {
        return currentValue.getValue(databaseValue)
    }

    override fun encode(value: TransactionStateEntity): String {
        return value.string
    }
}
