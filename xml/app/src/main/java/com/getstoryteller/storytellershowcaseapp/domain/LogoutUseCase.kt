package com.getstoryteller.storytellershowcaseapp.domain

import com.getstoryteller.storytellershowcaseapp.domain.ports.AmplitudeService
import com.getstoryteller.storytellershowcaseapp.domain.ports.SessionRepository

interface LogoutUseCase {
  fun logout()
}

class LogoutUseCaseImpl(
  private val sessionRepository: SessionRepository,
  private val amplitudeService: AmplitudeService,
) : LogoutUseCase {
  override fun logout() {
    sessionRepository.apiKey = null
    sessionRepository.userId = null
    amplitudeService.logout()
  }
}
