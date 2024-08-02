package com.cdcoding.domain

import com.cdcoding.data.repository.SessionRepository

class GetHasSessionUseCase(
    private val sessionRepository: SessionRepository,
) {

    operator fun invoke(): Boolean = sessionRepository.hasSession()
}