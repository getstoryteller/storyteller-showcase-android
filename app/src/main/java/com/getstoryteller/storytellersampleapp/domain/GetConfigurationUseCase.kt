package com.getstoryteller.storytellersampleapp.domain

import com.getstoryteller.storytellersampleapp.data.KeyValueDto
import com.getstoryteller.storytellersampleapp.data.TabDto
import com.getstoryteller.storytellersampleapp.data.repo.TenantRepository

interface GetConfigurationUseCase {
    suspend fun getConfiguration(): Config
}

class GetConfigurationUseCaseImpl(
    private val tenantRepository: TenantRepository
) : GetConfigurationUseCase {
    override suspend fun getConfiguration(): Config {
        val settings = tenantRepository.getTenantSettings()
        val languages = tenantRepository.getLanguages()
        val teams = tenantRepository.getTeams()
        val tabs = if (settings.tabsEnabled) {
            tenantRepository.getTabs()
        } else {
            emptyList()
        }
        return Config(
            topLevelCollectionId = settings.topLevelClipsCollection,
            tabsEnabled = settings.tabsEnabled,
            languages = languages,
            teams = teams,
            tabs = tabs
        )
    }
}

data class Config(
    val topLevelCollectionId: String?,
    val tabsEnabled: Boolean,
    val languages: List<KeyValueDto>,
    val teams: List<KeyValueDto>,
    val tabs: List<TabDto>
)