package com.getstoryteller.storytellershowcaseapp.amplitude

import android.content.Context
import com.amplitude.api.Amplitude
import com.getstoryteller.storytellershowcaseapp.BuildConfig
import com.getstoryteller.storytellershowcaseapp.services.SessionRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface AmplitudeService {
  fun init()

  fun logout()
}

class AmplitudeServiceImpl @Inject constructor(
  @ApplicationContext private val context: Context,
  private val sessionRepository: SessionRepository
) : AmplitudeService {
  override fun init() {
    Amplitude.getInstance().initialize(
      context,
      BuildConfig.AMPLITUDE_API_KEY,
      sessionRepository.userId
    )
  }

  override fun logout() {
    Amplitude.getInstance().setUserId(null)
    Amplitude.getInstance().clearUserProperties()
    Amplitude.getInstance().regenerateDeviceId()
  }
}
