package com.getstoryteller.storytellershowcaseapp.data

import com.getstoryteller.storytellershowcaseapp.remote.api.ApiService
import com.getstoryteller.storytellershowcaseapp.data.entities.KeyValueDto
import com.getstoryteller.storytellershowcaseapp.data.entities.StorytellerItemApiDto
import com.getstoryteller.storytellershowcaseapp.data.entities.TabDto
import com.getstoryteller.storytellershowcaseapp.data.entities.TenantSettingsDto
import com.getstoryteller.storytellershowcaseapp.domain.ports.TenantRepository

// This repository class wraps the API Service (following the principles of
// clean architecture)

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
