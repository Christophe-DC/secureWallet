package com.cdcoding.importwallet.presentation

import com.cdcoding.data.repository.ImportError
import com.cdcoding.model.ImportType
import com.cdcoding.model.NameRecord
import com.cdcoding.model.WalletType

data class ImportWalletState(
    val loading: Boolean = false,
    val error: String = "",
    val importType: ImportType = ImportType(WalletType.multicoin),
    val generatedNameIndex: Int = 0,
    val chainName: String = "",
    val walletName: String = "",
    val walletNameError: String = "",
    val data: String = "",
    val nameRecord: NameRecord? = null,
    val dataError: ImportError? = null,
    val isShowSafeMessage: Boolean = false,
)