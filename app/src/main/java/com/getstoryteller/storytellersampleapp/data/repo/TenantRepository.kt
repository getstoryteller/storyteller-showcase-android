package com.getstoryteller.storytellersampleapp.data.repo

import com.getstoryteller.storytellersampleapp.api.ApiService
import com.getstoryteller.storytellersampleapp.data.KeyValueDto
import com.getstoryteller.storytellersampleapp.data.StorytellerItemApiDto
import com.getstoryteller.storytellersampleapp.data.TabDto
import com.getstoryteller.storytellersampleapp.data.TenantSettingsDto

interface TenantRepository {
    suspend fun getTenantSettings(): TenantSettingsDto
    suspend fun getLanguages(): List<KeyValueDto>
    suspend fun getTeams(): List<KeyValueDto>
    suspend fun getTabs(): List<TabDto>
    suspend fun getTabForId(tabId: String): List<StorytellerItemApiDto>
    suspend fun getHomePage(): List<StorytellerItemApiDto>
}

class TenantRepositoryImpl(
    private val apiService: ApiService
) : TenantRepository {
    override suspend fun getTenantSettings(): TenantSettingsDto {
        return apiService.getTenantSettings()
            .data.let {
                TenantSettingsDto(
                    topLevelClipsCollection = it.topLevelClipsCollection,
                    tabsEnabled = it.tabsEnabled
                )
            }
    }

    override suspend fun getLanguages(): List<KeyValueDto> {
        return apiService.getLanguages()
            .data
    }

    override suspend fun getTeams(): List<KeyValueDto> {
        return apiService.getTeams()
            .data
    }

    override suspend fun getTabs(): List<TabDto> {
        return apiService.getTabs()
            .data
    }

    override suspend fun getTabForId(tabId: String): List<StorytellerItemApiDto> {
        return apiService.getTabById(tabId)
            .data
    }

    override suspend fun getHomePage(): List<StorytellerItemApiDto> {
        return apiService.getHomeItems()
            .data
    }
}