package com.cdcoding.importwallet.presentation

import com.cdcoding.model.NameRecord


sealed class ImportWalletIntent {
    data class OnImport(
        val name: String,
        val generatedName:String,
        val data: String,
        val nameRecord: NameRecord?,
        val onImported: () -> Unit
    ) : ImportWalletIntent()
}