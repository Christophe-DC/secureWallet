package com.cdcoding.wallet.repository

import com.cdcoding.common.utils.uuid4
import com.cdcoding.data.local.db.WalletDao
import com.cdcoding.local.db.AccountEntity
import com.cdcoding.local.db.WalletEntity
import com.cdcoding.wallet.client.WalletClient
import com.cdcoding.wallet.model.Account
import com.cdcoding.wallet.model.Chain
import com.cdcoding.wallet.model.ImportType
import com.cdcoding.wallet.model.Wallet
import com.cdcoding.wallet.model.WalletType
import com.cdcoding.wallet.validator.InvalidPhrase
import com.cdcoding.wallet.validator.InvalidWords
import com.cdcoding.wallet.validator.PhraseValidator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

class WalletRepository (
    private val walletsDao: WalletDao,
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
            walletsDao.insertWalletWithAccount(
                wallet = WalletEntity(
                    id = walletId,
                    name = walletName,
                    wallet_index = getNextWalletNumber().toLong(),
                    type = WalletType.view.name,
                ),
                accounts = listOf(
                    AccountEntity(
                        id = uuid4(),
                        address = address,
                        chain = chain.name,
                        derivationPath = "",
                        extendedPublicKey = null,
                        wallet_Id = walletId,
                    )
                )
            )
        }
    }




    suspend fun createWallet(walletName: String): Result<Wallet> {
        val phrase = walletClient.createWallet()
        //Result.success(HDWallet(128, "").mnemonic())
        val generatedNameIndex = getNextWalletNumber()
        val result = handlePhrase(ImportType(WalletType.multicoin), walletName, phrase)
        if (result.isFailure) {
            return result
        }
        val wallet = result.getOrNull() ?: return Result.failure(Exception("Unknown error"))
        return Result.success(wallet)
        /*importWalletOperator(ImportType(WalletType.multicoin), state.value.name, phrase)
        val result = when (importType.walletType) {
            WalletType.multicoin -> handlePhrase(importType, walletName, data)
            WalletType.single -> handlePhrase(importType, walletName, data)
            WalletType.view -> handleAddress(importType.chain!!, walletName, data)
        }
        if (result.isFailure) {
            return result
        }
        val wallet = result.getOrNull() ?: return Result.failure(Exception("Unknown error"))
        syncSubscription.invoke()
        assetsRepository.invalidateDefault(wallet.type, wallet, sessionRepository.getSession()?.currency ?: Currency.USD)
        sessionRepository.setWallet(wallet)
        return Result.success(wallet)*/
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

   /* private suspend fun handleAddress(chain: Chain, walletName: String, data: String): Result<Wallet> {
        if (addressValidate(data, chain).getOrNull() != true) {
            R.string.errors_create_wallet
            return Result.failure(ImportError.InvalidAddress)
        }
        val result = walletsRepository.addWatch(walletName, data, chain)
        val wallet = result.getOrNull()

        return if (result.isFailure || wallet == null) {
            Result.failure(ImportError.CreateError(result.exceptionOrNull()?.message ?: "Unknown error"))
        } else {
            Result.success(wallet)
        }
    }*/


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
        val walletId = uuid4()
        val walletEntity = WalletEntity(
            id = walletId,
            name = walletName,
            wallet_index = getNextWalletNumber().toLong(),
            type = WalletType.view.name,
        )
        val accountsEntities = accounts.map {
            AccountEntity(
                id = uuid4(),
                address = it.address,
                chain = it.chain.name,
                derivationPath = it.derivationPath,
                extendedPublicKey = it.extendedPublicKey,
                wallet_Id = walletId,
            )
        }
        walletsDao.insertWalletWithAccount(walletEntity, accountsEntities)

        val wallet = Wallet(
            id = walletEntity.id,
            name = walletEntity.name,
            index = walletEntity.wallet_index,
            type = WalletType.valueOf(walletEntity.type),
            accounts = accounts)
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