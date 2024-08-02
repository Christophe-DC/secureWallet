package com.cdcoding.database.db.adapter

import app.cash.sqldelight.ColumnAdapter
import com.cdcoding.database.db.model.AssetTypeEntity

class AssetTypeAdapter: ColumnAdapter<AssetTypeEntity, String> {

    private val currentValue by nonSynchronizedLazy {
        AssetTypeEntity.entries.associateBy { it.string }
    }

    override fun decode(databaseValue: String): AssetTypeEntity {
        return currentValue.getValue(databaseValue)
    }

    override fun encode(value: AssetTypeEntity): String {
        return value.string
    }
}
