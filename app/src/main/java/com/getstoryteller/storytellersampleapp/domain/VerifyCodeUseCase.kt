package com.getstoryteller.storytellersampleapp.domain

import com.getstoryteller.storytellersampleapp.data.TenantSettingsDto
import com.getstoryteller.storytellersampleapp.data.repo.AuthRepository
import com.getstoryteller.storytellersampleapp.services.SessionService
import com.getstoryteller.storytellersampleapp.services.StorytellerService
import java.util.UUID

interface VerifyCodeUseCase {
    suspend fun verifyCode(code: String): TenantSettingsDto
}

class VerifyCodeUseCaseImpl(
    private val authRepository: AuthRepository,
    private val sessionService: SessionService,
    private val storytellerService: StorytellerService
) : VerifyCodeUseCase {
    override suspend fun verifyCode(code: String): TenantSettingsDto {
        val settings = authRepository.verifyCode(code)
        sessionService.apiKey = settings.androidApiKey
        sessionService.userId = UUID.randomUUID().toString()
        storytellerService.initStoryteller()
        return TenantSettingsDto(
            topLevelClipsCollection = settings.topLevelClipsCollection,
            tabsEnabled = settings.tabsEnabled
        )
    }
}