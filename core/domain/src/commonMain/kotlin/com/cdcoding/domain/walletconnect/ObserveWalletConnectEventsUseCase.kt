package com.cdcoding.domain.walletconnect

import com.cdcoding.data.repository.WalletConnectRepository
import com.cdcoding.model.WcEvent
import kotlinx.coroutines.flow.Flow

class ObserveWalletConnectEventsUseCase(private val repo: WalletConnectRepository) {
    operator fun invoke(): Flow<WcEvent> = repo.events
}