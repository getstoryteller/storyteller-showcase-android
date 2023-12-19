package com.getstoryteller.storytellershowcaseapp.data.repo

import com.getstoryteller.storytellershowcaseapp.api.ApiService
import com.getstoryteller.storytellershowcaseapp.data.entities.TenantSettingsApiDto
import com.getstoryteller.storytellershowcaseapp.domain.ports.AuthRepository

// This repository class wraps the API Service (following the principles of
// clean architecture)

class AuthRepositoryImpl(
  private val apiService: ApiService
) : AuthRepository {
  override suspend fun verifyCode(code: String): TenantSettingsApiDto {
    return apiService.verifyCode(code)
      .data
  }
}
