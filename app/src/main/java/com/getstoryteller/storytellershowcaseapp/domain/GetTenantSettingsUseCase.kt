package com.getstoryteller.storytellershowcaseapp.domain

import com.getstoryteller.storytellershowcaseapp.data.entities.TenantSettingsDto
import com.getstoryteller.storytellershowcaseapp.domain.ports.TenantRepository
import com.getstoryteller.storytellershowcaseapp.services.StorytellerService

interface GetTenantSettingsUseCase {
  suspend fun getTenantSettings(): TenantSettingsDto
}

class GetTenantSettingsUseCaseImpl(
  private val tenantRepository: TenantRepository,
  private val storytellerService: StorytellerService
) : GetTenantSettingsUseCase {
  override suspend fun getTenantSettings(): TenantSettingsDto {
    storytellerService.initStoryteller()
    return tenantRepository.getTenantSettings()
  }
}
