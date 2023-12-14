package com.getstoryteller.storytellersampleapp.data.repo

import com.getstoryteller.storytellersampleapp.api.ApiService
import com.getstoryteller.storytellersampleapp.data.entities.TenantSettingsApiDto
import com.getstoryteller.storytellersampleapp.domain.ports.AuthRepository

class AuthRepositoryImpl(
  private val apiService: ApiService
) : AuthRepository {
  override suspend fun verifyCode(code: String): TenantSettingsApiDto {
    return apiService.verifyCode(code)
      .data
  }
}
