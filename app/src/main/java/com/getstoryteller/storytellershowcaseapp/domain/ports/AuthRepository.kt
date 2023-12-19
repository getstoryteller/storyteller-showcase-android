package com.getstoryteller.storytellershowcaseapp.domain.ports

import com.getstoryteller.storytellershowcaseapp.data.entities.TenantSettingsApiDto

interface AuthRepository {
    suspend fun verifyCode(code: String): TenantSettingsApiDto
}

