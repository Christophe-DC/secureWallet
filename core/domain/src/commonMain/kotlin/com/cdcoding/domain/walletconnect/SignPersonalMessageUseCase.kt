package com.cdcoding.domain.walletconnect

import com.cdcoding.data.repository.SessionRepository
import com.cdcoding.datastore.password.PasswordStore
import com.cdcoding.model.Chain
import com.cdcoding.model.ConfirmParams
import com.cdcoding.network.client.SignTransfer
import com.cdcoding.network.client.SignerPreload
import com.cdcoding.network.util.getOrNull
import com.cdcoding.wallet.operator.LoadPrivateKeyOperator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class SignPersonalMessageUseCase(
    private val sessionRepository: SessionRepository,
    private val passwordStore: PasswordStore,
    private val loadPrivateKeyOperator: LoadPrivateKeyOperator,
    private val signerPreload: SignerPreload,
    private val signTransfer: SignTransfer,
) {
    suspend operator fun invoke(
        messageParam: String,
    ): Result<ByteArray> {
        val session = sessionRepository.getSession() ?: return Result.failure(IllegalStateException("No active session"))
        /*
               val owner = session.wallet.accounts.firstOrNull { it.chain == Chain.Ethereum }
                   ?: return Result.failure(IllegalStateException("No Ethereum account in session"))


               val confirmParams = ConfirmParams(
                   type = ConfirmType.SignMessage,
                   message = messageParam
               )

               // 2) preload -> SignerParams
               val signerParams = withContext(Dispatchers.IO) {
                   signerPreload(
                       owner = owner,
                       params = confirmParams,
                   ).getOrNull()
               }
               */

        // 3) privateKey
        val password = passwordStore.getPassword(session.wallet.id)
            ?: return Result.failure(IllegalStateException("Password not found"))

        val privateKey = loadPrivateKeyOperator(session.wallet.id, Chain.Ethereum, password)
            ?: return Result.failure(IllegalStateException("privateKey is null"))

        // 4) message bytes
        val messageBytes = decodePersonalSignMessageParam(messageParam)

        // 5) signer
        // ➜ Option A: ton SignClient Ethereum sait signer via signMessage(input, privateKey)
        // (c’est ton second invoke(chain,input,pk))
        return signTransfer(
            chain = Chain.Ethereum,
            input = messageBytes,
            privateKey = privateKey
        )

        // ➜ Option B: si ton impl EVM a besoin de SignerParams pour signer le message,
        // alors tu utiliserais:
        // return signTransfer(signerParams, privateKey)
    }

    private fun decodePersonalSignMessageParam(messageParam: String): ByteArray {
        return if (messageParam.startsWith("0x", ignoreCase = true)) {
            hexToBytes(messageParam.removePrefix("0x"))
        } else {
            messageParam.encodeToByteArray()
        }
    }

    private fun hexToBytes(hex: String): ByteArray {
        val clean = hex.trim()
        require(clean.length % 2 == 0) { "Invalid hex length" }
        return ByteArray(clean.length / 2) { i ->
            val idx = i * 2
            clean.substring(idx, idx + 2).toInt(16).toByte()
        }
    }
}
