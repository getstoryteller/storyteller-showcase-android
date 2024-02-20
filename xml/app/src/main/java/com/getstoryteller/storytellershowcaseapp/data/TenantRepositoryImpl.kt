package com.getstoryteller.storytellershowcaseapp.data

import com.getstoryteller.storytellershowcaseapp.domain.ports.TenantRepository
import com.getstoryteller.storytellershowcaseapp.remote.api.ApiService
import com.getstoryteller.storytellershowcaseapp.remote.entities.StorytellerItemApiDto

// This repository class wraps the API Service (following the principles of
// clean architecture)

class TenantRepositoryImpl(
  private val apiService: ApiService,
) : TenantRepository {

  override suspend fun getHomePage(): List<StorytellerItemApiDto> {
    return apiService.getHomeItems()
      .data
  }
}
