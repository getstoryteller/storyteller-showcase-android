package com.getstoryteller.storytellersampleapp.domain

import com.getstoryteller.storytellersampleapp.services.SessionService

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