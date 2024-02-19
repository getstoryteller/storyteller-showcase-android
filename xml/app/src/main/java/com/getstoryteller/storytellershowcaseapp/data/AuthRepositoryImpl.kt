package com.getstoryteller.storytellershowcaseapp.data

import com.getstoryteller.storytellershowcaseapp.domain.ports.AuthRepository
import com.getstoryteller.storytellershowcaseapp.remote.api.ApiService
import com.getstoryteller.storytellershowcaseapp.remote.entities.TenantSettingsApiDto

// This repository class wraps the API Service (following the principles of
// clean architecture)

class AuthRepositoryImpl(
  private val apiService: ApiService,
) : AuthRepository {
  override suspend fun verifyCode(
    code: String,
  ): TenantSettingsApiDto {
    return apiService.verifyCode(code)
      .data
  }
}
