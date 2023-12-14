package com.getstoryteller.storytellersampleapp.domain.ports

import com.getstoryteller.storytellersampleapp.data.entities.TenantSettingsApiDto

interface AuthRepository {
    suspend fun verifyCode(code: String): TenantSettingsApiDto
}

