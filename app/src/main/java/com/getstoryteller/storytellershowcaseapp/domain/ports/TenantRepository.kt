package com.getstoryteller.storytellershowcaseapp.domain.ports

import com.getstoryteller.storytellershowcaseapp.remote.entities.KeyValueDto
import com.getstoryteller.storytellershowcaseapp.remote.entities.StorytellerItemApiDto
import com.getstoryteller.storytellershowcaseapp.remote.entities.TabDto
import com.getstoryteller.storytellershowcaseapp.remote.entities.TenantSettingsDto

interface TenantRepository {
  suspend fun getTenantSettings(): TenantSettingsDto
  suspend fun getLanguages(): List<KeyValueDto>
  suspend fun getTeams(): List<KeyValueDto>
  suspend fun getTabs(): List<TabDto>
  suspend fun getTabForId(tabId: String): List<StorytellerItemApiDto>
  suspend fun getHomePage(): List<StorytellerItemApiDto>
}
