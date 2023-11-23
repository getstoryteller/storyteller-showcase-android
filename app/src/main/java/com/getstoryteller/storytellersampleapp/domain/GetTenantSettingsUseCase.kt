package com.getstoryteller.storytellersampleapp.domain

import com.getstoryteller.storytellersampleapp.data.TenantSettingsDto
import com.getstoryteller.storytellersampleapp.data.repo.TenantRepository
import com.getstoryteller.storytellersampleapp.services.StorytellerService

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