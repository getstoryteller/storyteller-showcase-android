package com.getstoryteller.storytellershowcaseapp.domain.ports

import com.getstoryteller.storytellershowcaseapp.remote.entities.StorytellerItemApiDto

interface TenantRepository {

  suspend fun getHomePage(): List<StorytellerItemApiDto>
}
