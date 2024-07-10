package com.getstoryteller.storytellershowcaseapp.data

import com.getstoryteller.storytellershowcaseapp.domain.ports.TenantRepository
import com.getstoryteller.storytellershowcaseapp.remote.api.ApiService
import com.getstoryteller.storytellershowcaseapp.remote.entities.AttributeDto
import com.getstoryteller.storytellershowcaseapp.remote.entities.AttributeValueDto
import com.getstoryteller.storytellershowcaseapp.remote.entities.KeyValueDto
import com.getstoryteller.storytellershowcaseapp.remote.entities.StorytellerItemApiDto
import com.getstoryteller.storytellershowcaseapp.remote.entities.TabDto
import com.getstoryteller.storytellershowcaseapp.remote.entities.TenantSettingsDto

// This repository class wraps the API Service (following the principles of
// clean architecture)

class TenantRepositoryImpl(
  private val apiService: ApiService,
) : TenantRepository {
  override suspend fun getTenantSettings(): TenantSettingsDto {
    return apiService.getTenantSettings()
      .data.let {
        TenantSettingsDto(
          topLevelClipsCollection = it.topLevelClipsCollection,
          tabsEnabled = it.tabsEnabled,
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

  override suspend fun getTabForId(
    tabId: String,
  ): List<StorytellerItemApiDto> {
    return apiService.getTabById(tabId)
      .data
  }

  override suspend fun getHomePage(): List<StorytellerItemApiDto> {
    return apiService.getHomeItems()
      .data
  }

  override suspend fun getAttributes(): Map<AttributeDto, List<AttributeValueDto>> {
    return apiService.getAttributesAndValues()
  }
}
