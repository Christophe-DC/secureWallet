package com.cdcoding.session.di

import com.cdcoding.session.repository.DefaultSessionRepository
import com.cdcoding.session.repository.SessionRepository
import org.koin.dsl.module

val sessionModule = module {
    single<SessionRepository> { DefaultSessionRepository( get()) }
}
