package com.getstoryteller.storytellershowcaseapp.domain

import com.getstoryteller.storytellershowcaseapp.amplitude.AmplitudeService
import com.getstoryteller.storytellershowcaseapp.services.SessionRepository

interface LogoutUseCase {
  fun logout()
}

class LogoutUseCaseImpl(
    private val sessionRepository: SessionRepository,
    private val amplitudeService: AmplitudeService
) : LogoutUseCase {
  override fun logout() {
    sessionRepository.apiKey = null
    sessionRepository.userId = null
    amplitudeService.logout()
  }
}
