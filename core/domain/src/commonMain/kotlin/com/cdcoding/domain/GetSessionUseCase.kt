package com.cdcoding.domain

import com.cdcoding.model.Session
import com.cdcoding.data.repository.SessionRepository
import kotlinx.coroutines.flow.Flow

class GetSessionUseCase(
    private val sessionRepository: SessionRepository,
) {

    operator fun invoke(): Flow<Session?> = sessionRepository.session()
}