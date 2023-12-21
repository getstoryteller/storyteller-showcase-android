package com.getstoryteller.storytellershowcaseapp.domain

import com.getstoryteller.storytellershowcaseapp.amplitude.AmplitudeService
import com.getstoryteller.storytellershowcaseapp.services.SessionService

interface LogoutUseCase {
  fun logout()
}

class LogoutUseCaseImpl(
  private val sessionService: SessionService,
  private val amplitudeService: AmplitudeService
) : LogoutUseCase {
  override fun logout() {
    sessionService.apiKey = null
    sessionService.userId = null
    amplitudeService.logout()
  }
}
