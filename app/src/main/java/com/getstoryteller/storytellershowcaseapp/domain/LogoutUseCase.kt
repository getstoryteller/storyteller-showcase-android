package com.getstoryteller.storytellershowcaseapp.domain

import com.getstoryteller.storytellershowcaseapp.services.SessionService

interface LogoutUseCase {
    fun logout()
}

class LogoutUseCaseImpl(
    private val sessionService: SessionService
) : LogoutUseCase {
    override fun logout() {
        sessionService.apiKey = null
        sessionService.userId = null
    }
}
