package com.cdcoding.data.local.db.adapter

import app.cash.sqldelight.ColumnAdapter
import com.cdcoding.data.local.db.model.ChainEntity

class AccountChainAdapter: ColumnAdapter<ChainEntity, String> {

    private val currentValue by nonSynchronizedLazy {
        ChainEntity.entries.associateBy { it.string }
    }

    override fun decode(databaseValue: String): ChainEntity {
        return currentValue.getValue(databaseValue)
    }

    override fun encode(value: ChainEntity): String {
        return value.string
    }
}

/**
 * Creates a lazily initialized property using a non-synchronized initializer.
 *
 * @param initializer Lambda that computes the initial value of the property.
 * @return Lazy property with non-synchronized initialization.
 */
inline fun <T> nonSynchronizedLazy(noinline initializer: () -> T): Lazy<T> {
    return lazy(LazyThreadSafetyMode.NONE, initializer)
}
