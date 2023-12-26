package com.getstoryteller.storytellershowcaseapp.domain.ports

import com.getstoryteller.storytellershowcaseapp.remote.entities.TenantSettingsApiDto

interface AuthRepository {
  suspend fun verifyCode(code: String): TenantSettingsApiDto
}
