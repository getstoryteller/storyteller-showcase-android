package com.getstoryteller.storytellersampleapp.domain.ports

import com.getstoryteller.storytellersampleapp.data.entities.KeyValueDto
import com.getstoryteller.storytellersampleapp.data.entities.StorytellerItemApiDto
import com.getstoryteller.storytellersampleapp.data.entities.TabDto
import com.getstoryteller.storytellersampleapp.data.entities.TenantSettingsDto

interface TenantRepository {
  suspend fun getTenantSettings(): TenantSettingsDto
  suspend fun getLanguages(): List<KeyValueDto>
  suspend fun getTeams(): List<KeyValueDto>
  suspend fun getTabs(): List<TabDto>
  suspend fun getTabForId(tabId: String): List<StorytellerItemApiDto>
  suspend fun getHomePage(): List<StorytellerItemApiDto>
}
