package com.getstoryteller.storytellershowcaseapp.domain

import com.getstoryteller.storytellershowcaseapp.data.entities.TenantSettingsDto
import com.getstoryteller.storytellershowcaseapp.domain.ports.AuthRepository
import com.getstoryteller.storytellershowcaseapp.services.SessionService
import com.getstoryteller.storytellershowcaseapp.services.StorytellerService
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
    storytellerService.updateCustomAttributes()
    return TenantSettingsDto(
      topLevelClipsCollection = settings.topLevelClipsCollection,
      tabsEnabled = settings.tabsEnabled
    )
  }
}
