package com.getstoryteller.storytellershowcaseapp.data

import android.content.Context
import com.amplitude.android.Amplitude
import com.amplitude.android.Configuration
import com.amplitude.android.events.Identify
import com.getstoryteller.storytellershowcaseapp.BuildConfig
import com.getstoryteller.storytellershowcaseapp.domain.ports.AmplitudeService
import com.getstoryteller.storytellershowcaseapp.domain.ports.SessionRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AmplitudeServiceImpl @Inject constructor(
  @ApplicationContext private val context: Context,
  private val sessionRepository: SessionRepository,
) : AmplitudeService {
  private var client: Amplitude? = null
  override fun init() {
    client = Amplitude(
      Configuration(
        apiKey = BuildConfig.AMPLITUDE_API_KEY,
        context = context,
      ),
    ).apply {
      val userProperties = Identify()
      userProperties.set("userId", sessionRepository.userId ?: "")
      identify(userProperties)
    }
  }

  override fun logout() {
    client?.flush()
    client?.identify(Identify().set("userId", ""))
  }

  override fun track(
    event: String,
    data: Map<String, Any?>,
  ) {
    client?.track(event, data)
  }
}
