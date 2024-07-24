package com.cdcoding.wallet.repository

import com.cdcoding.common.utils.uuid4
import com.cdcoding.data.local.db.SessionDao
import com.cdcoding.data.local.db.WalletDao
import com.cdcoding.model.Account
import com.cdcoding.model.Chain
import com.cdcoding.model.ImportType
import com.cdcoding.model.Wallet
import com.cdcoding.model.WalletType
import com.cdcoding.wallet.client.WalletClient
import com.cdcoding.wallet.validator.InvalidPhrase
import com.cdcoding.wallet.validator.InvalidWords
import com.cdcoding.wallet.validator.PhraseValidator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

class WalletRepository (
    private val walletsDao: WalletDao,
    private val sessionDao: SessionDao,
    private val walletClient: WalletClient,
    private val phraseValidator: PhraseValidator,
) {
    suspend fun getNextWalletNumber(): Int {
        return (walletsDao.getAllWallets().firstOrNull()?.size ?: 0) + 1
    }

    suspend fun getAllWallets() = walletsDao.getAllWallets()

    suspend fun insertWallet(walletName: String, address: String, chain: Chain): Result<Unit> {
        return runCatching {
            val walletId = uuid4()
            /*walletsDao.insertWalletWithAccount(
                wallet = WalletEntity(
                    id = walletId,
                    name = walletName,
                    wallet_index = getNextWalletNumber().toLong(),
                    type = WalletType.multicoin,
                ),
                accounts = listOf(
                    AccountEntity(
                        id = uuid4(),
                        address = address,
                        chain = chain.name,
                        derivationPath = "",
                        extendedPublicKey = null
                    )
                )
            )*/
        }
    }

    suspend fun createWallet(walletName: String): Result<Wallet> {
        val phrase = walletClient.createWallet()
        val result = handlePhrase(ImportType(WalletType.multicoin), walletName, phrase)
        if (result.isFailure) {
            return result
        }
        val wallet = result.getOrNull() ?: return Result.failure(Exception("Unknown error"))
        sessionDao.setWallet(wallet)
        return Result.success(wallet)
    }

    private suspend fun handlePhrase(importType: ImportType, walletName: String, rawData: String): Result<Wallet> {
        val cleanedData = rawData.trim().split("\\s+".toRegex()).joinToString(" ") { it.trim() }
        val validateResult = phraseValidator(cleanedData)
        if (validateResult.isFailure || validateResult.getOrNull() != true) {
            val error = validateResult.exceptionOrNull() ?: InvalidPhrase
            return when (error) {
                is InvalidWords -> Result.failure(ImportError.InvalidWords(error.words))
                else -> Result.failure(ImportError.InvalidationSecretPhrase)
            }
        }
        val result = addPhrase(walletName, cleanedData, importType.walletType, importType.chain)
        val wallet = result.getOrNull()
        return if (result.isFailure || wallet == null) {
            result
        } else {
            result
            /*val password = passwordStore.createPassword(wallet.id)
            val storeResult = storePhraseOperator(wallet.id, cleanedData, password)
            if (storeResult.isSuccess) {
                result
            } else {
                walletsRepository.removeWallet(wallet.id)
                Result.failure(storeResult.exceptionOrNull() ?: ImportError.CreateError("Unknown error"))
            }*/
        }
    }


    suspend fun addPhrase(
        walletName: String,
        data: String,
        type: WalletType,
        chain: Chain?
    ): Result<Wallet> = withContext(Dispatchers.IO) {
        val accounts = mutableListOf<Account>()
        val chains =
            if (type == WalletType.single && chain != null) listOf(chain) else Chain.entries
        for (item in chains) {
            accounts.add(walletClient.createWallet(data, item))
        }

        val wallet = Wallet(
            id = uuid4(),
            name = walletName,
            index = getNextWalletNumber().toLong(),
            type = WalletType.multicoin,
            accounts = accounts
        )

        walletsDao.insertWalletWithAccount(wallet, accounts)
        Result.success(wallet)
    }

    suspend fun updateWallet(wallet: Wallet) {
        // walletsLocalSource.updateWallet(wallet)
    }

    // suspend fun removeWallet(walletId: String) = walletsLocalSource.removeWallet(walletId)

    // suspend fun getWallet(walletId: String): Result<Wallet> = walletsLocalSource.getWallet(walletId)

}

sealed class ImportError(message: String = "") : Exception(message) {

    data object InvalidationSecretPhrase : ImportError()

    class InvalidWords(val words: List<String>) : ImportError()

    data object InvalidAddress : ImportError()

    class CreateError(message: String) : ImportError(message)
}