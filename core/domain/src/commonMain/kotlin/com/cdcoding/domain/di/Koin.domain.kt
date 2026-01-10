package com.cdcoding.domain.di

import com.cdcoding.domain.CreateWalletUseCase
import com.cdcoding.domain.DeleteWalletUseCase
import com.cdcoding.domain.GetAssetsByQueryUseCase
import com.cdcoding.domain.GetAssetsByWalletUseCase
import com.cdcoding.domain.GetHasSessionUseCase
import com.cdcoding.domain.GetNextWalletNumberUseCase
import com.cdcoding.domain.GetSessionUseCase
import com.cdcoding.domain.SyncTransactionUseCase
import com.cdcoding.domain.walletconnect.ApproveWalletConnectProposalUseCase
import com.cdcoding.domain.walletconnect.DisconnectAllWalletConnectUseCase
import com.cdcoding.domain.walletconnect.DisconnectWalletConnectUseCase
import com.cdcoding.domain.walletconnect.ObserveWalletConnectEventsUseCase
import com.cdcoding.domain.walletconnect.PairWalletConnectUseCase
import com.cdcoding.domain.walletconnect.PreviewWalletConnectApprovedChainsUseCase
import com.cdcoding.domain.walletconnect.RejectWalletConnectProposalUseCase
import com.cdcoding.domain.walletconnect.RespondWalletConnectErrorUseCase
import com.cdcoding.domain.walletconnect.RespondWalletConnectResultUseCase
import com.cdcoding.domain.walletconnect.SignPersonalMessageUseCase
import org.koin.dsl.module

val useCaseDomainModule = module {
    single<CreateWalletUseCase> { CreateWalletUseCase(get()) }
    single<GetNextWalletNumberUseCase> { GetNextWalletNumberUseCase(get()) }
    single<GetHasSessionUseCase> { GetHasSessionUseCase(get()) }
    single<GetAssetsByWalletUseCase> { GetAssetsByWalletUseCase(get()) }
    single<GetSessionUseCase> { GetSessionUseCase(get()) }
    single<GetAssetsByQueryUseCase> { GetAssetsByQueryUseCase(get(), get()) }
    single<DeleteWalletUseCase> { DeleteWalletUseCase(get(), get(), get()) }
    single<SyncTransactionUseCase> { SyncTransactionUseCase(get(), get(), get(), get(), get()) }
    single { ObserveWalletConnectEventsUseCase(get()) }
    single { PairWalletConnectUseCase(get()) }
    single { PreviewWalletConnectApprovedChainsUseCase(get(), get()) }
    single { ApproveWalletConnectProposalUseCase(get(), get()) }
    single { RejectWalletConnectProposalUseCase(get()) }
    single { RespondWalletConnectResultUseCase(get()) }
    single { RespondWalletConnectErrorUseCase(get()) }
    single { DisconnectWalletConnectUseCase(get()) }
    single { DisconnectAllWalletConnectUseCase(get()) }
    single { SignPersonalMessageUseCase(get(), get(), get(), get(), get()) }
}
