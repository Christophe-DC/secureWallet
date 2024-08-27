package com.cdcoding.database.db.adapter

import app.cash.sqldelight.ColumnAdapter
import com.cdcoding.database.db.model.TransactionDirectionEntity

class TransactionDirectionAdapter: ColumnAdapter<TransactionDirectionEntity, String> {

    private val currentValue by nonSynchronizedLazy {
        TransactionDirectionEntity.entries.associateBy { it.string }
    }

    override fun decode(databaseValue: String): TransactionDirectionEntity {
        return currentValue.getValue(databaseValue)
    }

    override fun encode(value: TransactionDirectionEntity): String {
        return value.string
    }
}
