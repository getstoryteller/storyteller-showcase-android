package com.getstoryteller.storytellersampleapp.data.repo

import com.getstoryteller.storytellersampleapp.api.ApiService
import com.getstoryteller.storytellersampleapp.data.TenantSettingsApiDto

interface AuthRepository {
    suspend fun verifyCode(code: String): TenantSettingsApiDto
}

class AuthRepositoryImpl(
    private val apiService: ApiService
) : AuthRepository {
    override suspend fun verifyCode(code: String): TenantSettingsApiDto {
        return apiService.verifyCode(code)
            .data
    }
}